package annotation.framework;

import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class RuntimeFramework {
	public static void main(String args[]) {
		System.out.println(Utils.createTable(Bean.class));
	}
}

class Utils {
	public static String createTable(Class<?> bean) {
		String tableName = getTableName(bean);
		List<TableField> columns = getColumns(bean);
		System.out.println(tableName);
		System.out.println(columns.size());
		if(tableName != null && !tableName.equals("") && !columns.isEmpty()) {
			StringBuilder createTableSql = new StringBuilder("create table");
			createTableSql.append(tableName);
			createTableSql.append("(");

			for(int i = 0; i < columns.size(); i++) {
				TableField column = columns.get(i);
				createTableSql.append(column.name);
				createTableSql.append(" ");
				createTableSql.append(column.type);
				if(i != columns.size() - 1) {
					createTableSql.append(",");
				}
			}

			createTableSql.append(")");
			return createTableSql.toString();
		}
		return null;
	}

	private static class TableField {
		final String type;
		final String name;

		public TableField(String type, String name) {
			this.type = type;
			this.name = name;
		}
	}

	private static List<TableField> getColumns(Class<?> bean) {
		List<TableField> columns = new ArrayList<TableField>();
		Field[] fields = bean.getDeclaredFields();
		if(fields != null) {
			for(int i = 0; i < fields.length; i++) {
				Field field = fields[i];
				if(field.isAnnotationPresent(Column.class)) {
					String name = null;
					Column column = field.getAnnotation(Column.class);
                    name = column.name();
                    String type = null;
                    if (int.class.isAssignableFrom(field.getType())) {
                        type = "integer";
                    } else if (String.class.isAssignableFrom(field.getType())) {
                        type = "text";
                    } else {
                        throw new RuntimeException("unspported type=" + field.getType().getSimpleName());
                    }
                    columns.add(new TableField(type, name));
				}
			}
		}
		return columns;
	}

	private static String getTableName(Class<?> bean) {
		String name = null;
		//判断是否有Table注解
		if (bean.isAnnotationPresent(Table.class)) {
			//获取注解对象
			Annotation annotation = bean.getAnnotation(Table.class);
			try {
				//获取注解@Table所对应的name
				Method method = Table.class.getMethod("name");
				name = (String) method.invoke(annotation);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return name;
	}
}
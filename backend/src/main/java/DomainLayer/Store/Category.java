package DomainLayer.Store;

import Util.ExceptionsEnum;

public enum Category {
    ELECTRONICS,
    CLOTHING,
    FOOD,
    HOME,
    BOOKS,
    TOYS,
    ALCOHOL,
    DAIRY;

    public static Category fromString(String categoryStr) {
        if (categoryStr == null)
            return null;
        try {
            return Category.valueOf(categoryStr.toUpperCase());
        } catch (Exception e) {
            throw new RuntimeException(ExceptionsEnum.categoryNotExist.toString());
        }
    }
}

package org.exceptions;


public class EntityNotFoundMessage{
    public static final String DEFAULT_ENTITY_NOT_FOUND = "Entity not found";
    public static final String DEFAULT_PREFIX = "Could not find";
    public static final String DEFAULT_SUFFIX = "with id:";
    public static String createMessage(Long entityId, Class<?> classType){
        return DEFAULT_PREFIX + " " + classType.getSimpleName() + " " + DEFAULT_SUFFIX + " " + entityId;
    }
}

package org.smart4j.framework.annotation;

import java.lang.annotation.*;

/**
 * @author Liqn
 * @create 2017-07-17 19:54
 **/

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Aspect {

    Class<? extends Annotation> value();
}

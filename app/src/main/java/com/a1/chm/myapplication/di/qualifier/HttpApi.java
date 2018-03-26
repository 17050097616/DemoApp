package com.a1.chm.myapplication.di.qualifier;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;

import javax.inject.Qualifier;
import static java.lang.annotation.RetentionPolicy.RUNTIME;


/**
 * @author chm on 2017/12/1 0001
 */

@Qualifier
@Documented
@Retention(RUNTIME)
public @interface HttpApi {
}

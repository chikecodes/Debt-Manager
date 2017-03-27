package com.chikeandroid.debtmanager20.data.source;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Qualifier;

/**
 * Created by Chike on 3/27/2017.
 */
@Qualifier
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface Local {
}

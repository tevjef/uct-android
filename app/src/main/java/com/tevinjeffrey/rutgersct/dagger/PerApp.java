package com.tevinjeffrey.rutgersct.dagger;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import javax.inject.Scope;

@Scope
@Retention(RetentionPolicy.SOURCE)
public @interface PerApp { }

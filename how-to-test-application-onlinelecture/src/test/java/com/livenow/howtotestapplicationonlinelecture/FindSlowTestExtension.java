package com.livenow.howtotestapplicationonlinelecture;

import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.BeforeTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.lang.reflect.Method;

public class FindSlowTestExtension implements BeforeTestExecutionCallback, AfterTestExecutionCallback {

    private final long threshold;

    public FindSlowTestExtension(long threshold) {
        this.threshold = threshold;
    }

    @Override
    public void beforeTestExecution(ExtensionContext context) throws Exception {
        final ExtensionContext.Store store = getStore(context);
        store.put("START_TIME", System.currentTimeMillis());
    }

    @Override
    public void afterTestExecution(ExtensionContext context) throws Exception {
        final Method requiredTestMethod = context.getRequiredTestMethod();
        final SlowTest annotation = requiredTestMethod.getAnnotation(SlowTest.class);

        final String testMethodName = requiredTestMethod.getName();
        final ExtensionContext.Store store = getStore(context);
        final Long start_time = store.remove("START_TIME", long.class);
        final long duration = System.currentTimeMillis() - start_time;
        if (duration > threshold && annotation == null) {
            System.out.printf("Please consider mark method [%s] with @SlowTest", testMethodName);
        }
    }

    private ExtensionContext.Store getStore(ExtensionContext context) {
        final String testClassName = context.getRequiredTestClass().getName();
        final String testMethodName = context.getRequiredTestMethod().getName();
        final ExtensionContext.Store store = context.getStore(ExtensionContext.Namespace.create(testClassName, testMethodName));
        return store;
    }
}

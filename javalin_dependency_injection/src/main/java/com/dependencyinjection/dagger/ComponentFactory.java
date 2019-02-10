package com.dependencyinjection.dagger;

public class ComponentFactory {

    private MyModuleComponent myModuleComponent;

    public ComponentFactory() {
        myModuleComponent = DaggerMyModuleComponent.create();
    }

    public MyModuleComponent factory() {
        return myModuleComponent;
    }
}

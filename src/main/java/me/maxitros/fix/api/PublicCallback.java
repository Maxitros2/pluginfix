package me.maxitros.fix.api;

import me.maxitros.dbapi.api.CallBack;

public class PublicCallback<V extends Boolean, T extends Throwable> implements CallBack {
    public Object object;
    public PublicCallback(){}
    public PublicCallback(Object o){
        this.object=o;
    }

    @Override
    public void call(Object o, Throwable throwable) {
        this.object=o;
    }
}

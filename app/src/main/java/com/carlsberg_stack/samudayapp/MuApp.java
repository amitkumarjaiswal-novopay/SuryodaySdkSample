package com.carlsberg_stack.samudayapp;

import in.novopay.supportlib.SupportLibApplication;
import in.novopay.util.utils.ConfigUtil;
import io.realm.Realm;
import io.realm.RealmConfiguration;

public class MuApp extends SupportLibApplication {
    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder()
                .deleteRealmIfMigrationNeeded()
                .name(ConfigUtil.REALM_FILE_NAME)
                .modules(Realm.getDefaultModule()/*, new DracarysModule()*/)
                .schemaVersion(ConfigUtil.REALM_SCHEMA_VERSION)
                .build();
        Realm.setDefaultConfiguration(realmConfiguration);
    }
}

# SuryodaySdkSample

Has a dependancy of mavenLocal.

1. Install mavel in your machine (Google for steps)
2. See https://github.com/khoslalabs/novopay-platform-thor/tree/sb_sdk_maven_local for setting up the local maven and generating the library sdk 

## In Project Level Gradle

### For Local Build
```
allprojects {
    repositories {
        google()
        jcenter()

        maven {
            url "https://jfrog-repository.novopay.in/artifactory/libs-release/"
            credentials{
              username = "shruthi.n@novopay.in" // The publisher user name
              password = "N0v0WQSX678" // The publisher password
          }
//            url "C:/Users/Admin/.m2/repository"
        }
    }
}
```

### For Jfrog Build
```
allprojects {
    repositories {
        ...
        maven {
            url "https://jfrog-repository.novopay.in/artifactory/libs-release/"
            credentials{
              username = "shruthi.n@novopay.in" // The publisher user name
              password = "N0v0WQSX678" // The publisher password
          }
        }
    }
}
```

## In App Level Gradle
```
implementation "in.novopay.sbagency:app:latest-version"
```

## In Your Activity
```
import in.novopay.sbagency.SuryodaySdk;
import in.novopay.sbagency.SuryodaySdkStatus;


public class MainActivity extends AppCompatActivity {
    API_KEY = "1579514199220"
    NUM_HASHED_BYTES = 9
    NUM_BASE64_CHAR = 11
    HASH_TYPE = "SHA-256"
...

        SuryodaySdk.launch(
                this,
                mobileNum,
                BuildConfig.API_KEY,
                SuryodaySdk.SuryodayaEnvironment.QA,
                getAppSignatures().get(0),
                "debug_app",YourLoginActivityName.class);
}
```

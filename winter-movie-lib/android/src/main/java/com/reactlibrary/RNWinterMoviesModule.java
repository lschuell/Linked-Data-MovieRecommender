
package com.reactlibrary;

import android.widget.Toast;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.WritableArray;
import com.facebook.react.bridge.WritableNativeArray;

import de.uni_mannheim.informatik.dws.semtec.Extractor.Movie;
import de.uni_mannheim.informatik.dws.semtec.Recommender.Models.LinearCombinationRule;

public class RNWinterMoviesModule extends ReactContextBaseJavaModule {

  private final ReactApplicationContext reactContext;

  public RNWinterMoviesModule(ReactApplicationContext reactContext) {
    super(reactContext);
    this.reactContext = reactContext;
  }

  @Override
  public String getName() {
    return "RNWinterMovies";
  }

  @ReactMethod
  public void runLCModel(String identifier, Callback successCallback) throws Exception{
    WritableArray array = new WritableNativeArray();
    Movie movies[] = LinearCombinationRule.runLCRule(identifier);
    for(int i=0; i<10; i++) {
      array.pushString(movies[i].getName());
    }
    try {
      successCallback.invoke(array);
    } catch (Exception e) {
      Toast.makeText(getReactApplicationContext(), "Could not fetch Movies", Toast.LENGTH_LONG).show();
    }
  }

}
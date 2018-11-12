
# react-native-winter-movie-lib

## Getting started

`$ npm install react-native-winter-movie-lib --save`

### Mostly automatic installation

`$ react-native link react-native-winter-movie-lib`

### Manual installation


#### iOS

1. In XCode, in the project navigator, right click `Libraries` ➜ `Add Files to [your project's name]`
2. Go to `node_modules` ➜ `react-native-winter-movie-lib` and add `RNWinterMovieLib.xcodeproj`
3. In XCode, in the project navigator, select your project. Add `libRNWinterMovieLib.a` to your project's `Build Phases` ➜ `Link Binary With Libraries`
4. Run your project (`Cmd+R`)<

#### Android

1. Open up `android/app/src/main/java/[...]/MainActivity.java`
  - Add `import com.reactlibrary.RNWinterMovieLibPackage;` to the imports at the top of the file
  - Add `new RNWinterMovieLibPackage()` to the list returned by the `getPackages()` method
2. Append the following lines to `android/settings.gradle`:
  	```
  	include ':react-native-winter-movie-lib'
  	project(':react-native-winter-movie-lib').projectDir = new File(rootProject.projectDir, 	'../node_modules/react-native-winter-movie-lib/android')
  	```
3. Insert the following lines inside the dependencies block in `android/app/build.gradle`:
  	```
      compile project(':react-native-winter-movie-lib')
  	```

#### Windows
[Read it! :D](https://github.com/ReactWindows/react-native)

1. In Visual Studio add the `RNWinterMovieLib.sln` in `node_modules/react-native-winter-movie-lib/windows/RNWinterMovieLib.sln` folder to their solution, reference from their app.
2. Open up your `MainPage.cs` app
  - Add `using Winter.Movie.Lib.RNWinterMovieLib;` to the usings at the top of the file
  - Add `new RNWinterMovieLibPackage()` to the `List<IReactPackage>` returned by the `Packages` method


## Usage
```javascript
import RNWinterMovieLib from 'react-native-winter-movie-lib';

// TODO: What to do with the module?
RNWinterMovieLib;
```
  
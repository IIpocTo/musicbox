# Musicbox

Musicbox is a web application for listenting to music. It is developed in purpose of porting Spotify possibilities with some extra features into Russian market. Musicbox is totally free of use.

## Getting Started

For getting started just follow installing instruction for tuning the environment and launching the server. Then simply got to ``` http://127.0.0.1:3000 ``` to get onto the site.

### Prerequisites

* [SBT](https://www.scala-sbt.org)
* [NPM](https://www.npmjs.com)
* [NODE](https://nodejs.org/en/)
* Python 2.7

### Installing

Make sure you have prerequisites installed. Then execute the following instructions from the project folder. Replace calling ``` sudo ``` on windows machine with nothing using admin rights.

```
cd scripts
sudo python setup.py install
cd ../frontend
npm i
npm run build
cd ../backend
sbt compile
```
Now you are ready to launch the server and the client. To run the server call ``` sbt run ``` from the backend folder. To run the client call ``` npm run dev ``` to run the client in development mode or ``` npm start ``` to run in production.

## Running the tests

To run tests from the project make calls from both backend (``` sbt test ```) and frontend (``` npm t ```) folders.

## Built With

* [Nuxt.js](https://ru.nuxtjs.org) - The framework used
* [Webpack](https://webpack.js.org) - Assets bundler

## Contributing

Musicbox is open for pull requests from anyone.

## Versioning

* 0.0.1 Version

## Authors

* **Alexander Filshin** - *Initial work* - [IIpocTo](https://github.com/IIpocTo)
* **George Carpow** - *Initial work* - [eakarpov](https://github.com/eakarpov)
* **Nikita Ivanov** - *Initial work* - [BobNobrain](https://github.com/BobNobrain)
* **Daniil Azarnov** - *Initial work* - [DanilAzarnov](https://github.com/DanilAzarnov)

## License

This project is licensed under the Apache License - see the [LICENSE.md](LICENSE) file for details

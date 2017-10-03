[![Made by Styczynsky Digital Systems][badge sts]][link isis97]


# ![Logo][logo] Weatherly &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; :cloud: [![Download][badge download]][link download latest] :zap:

&nbsp;
Nice looking weather fetcher written in Java
 
 
 
 
 
## Installation/Usage
**[Download latest release from here.][link download latest]** 

Then unzip it. 
Move the application files somewhere and run `weatherly.exe` file. 

You can also run `java -jar weatherly.jar` on X-nix machines.


## Screenshots

![Screenshot 2][screenshot 2]

## Building

1. Make sure you have `jdk` and `maven` installed.
2. Then do `mvn clean compile` to compile sources.
3. Then `mvn exec:java` to execute compiled sources.
4. Execute `mvn package` to generate release files available in `target/release` folder.

## Customization

### Custom meters layout

Meters layout is defined in `meters.xml` file. 

You can edit it to create custom layout of temperature/wind direction/wind speed etc. controls. :+1: :tada: 

The exampel file looks like this:
```xml
<?xml version="1.0" encoding="UTF-8"?>

<meters>
	<weatherly.meters.GeneralMeter type="weather"></weatherly.meters.GeneralMeter>
	<weatherly.meters.GeneralMeter type="temperature"></weatherly.meters.GeneralMeter>
	<weatherly.meters.GeneralMeter type="wind"></weatherly.meters.GeneralMeter>
	<weatherly.meters.GeneralMeter type="humidity"></weatherly.meters.GeneralMeter>
	<weatherly.meters.GeneralMeter type="pressure"></weatherly.meters.GeneralMeter>
	<weatherly.meters.GeneralMeter
		airQualityProperties="PM10,PM2.5" type="airQuality"></weatherly.meters.GeneralMeter>
	<weatherly.meters.GeneralMeter type="sunrise"></weatherly.meters.GeneralMeter>
	<weatherly.meters.GeneralMeter type="sunset"></weatherly.meters.GeneralMeter>
</meters>
```

Or displaying only sunrise, sunset and weather:

```xml
<?xml version="1.0" encoding="UTF-8"?>

<meters>
	<weatherly.meters.GeneralMeter type="weather"></weatherly.meters.GeneralMeter>
	<weatherly.meters.GeneralMeter type="sunrise"></weatherly.meters.GeneralMeter>
	<weatherly.meters.GeneralMeter type="sunset"></weatherly.meters.GeneralMeter>
</meters>
```

Available build-in meter types for `GeneralMeter` are:
* <b>weather</b> (displays general weather type)
* <b>temperature</b> (displays temperature)
* <b>wind</b> (displays wind direction)
* <b>humidity</b> (displays air humidity)
* <b>pressure</b> (displays air pressure at ground or sea level)
* <b>sunrise/sunset</b> (displays calendar sunrise/sunset time)
* <b>airQuality</b> (displays gases and substances present in air) 

### Custom data sources

The data sources can be configured editing `sources.xml` file.

The example file looks like this:

```xml

<?xml version="1.0" encoding="UTF-8"?>

<sources interval="360">
	<source provider="weatherly.datasource.sources.PowietrzeGiosSource"></source>
	<source provider="weatherly.datasource.sources.OpenWeatherMapSource"></source>
	<source provider="weatherly.datasource.sources.MeteoWawSource"></source>
</sources>

```

![Screenshot 1][screenshot 1]

[badge download]: https://img.shields.io/badge/-download_me!-green.svg?style=flat-square&logoWidth=10&logo=data%3Aimage%2Fpng%3Bbase64%2CiVBORw0KGgoAAAANSUhEUgAAABkAAAArCAYAAACNWyPFAAAABmJLR0QA%2FwD%2FAP%2BgvaeTAAAACXBIWXMAAA7DAAAOwwHHb6hkAAAAB3RJTUUH4AgTDjEFFOXcpQAAAM1JREFUWMPt2EsOgzAMBFDPJHD%2F80Jid1G1KpR8SqKu7C2QJzwWsoCZSWedb0Tvg5Q%2FlCOOOOKII4444ogjjvxW8bTjYtK57zNTSoCdNm5VBcmRhdua7SJpKaXhN2hmEmO0fd%2BnANXgl2WxbduGAVUFVbUY9rquPVARyDmDpJCktKBK66pACOE5Ia%2FhUlUhaTPm9xM4ZEJScs6YDXwFH0IYgq6Ay%2Bm6C5WAQyYXo9edUQ2oIr1Q5TPUh4iImJkAsMI1AO3O4u4fiV5AROQBGVB7Fu2akxMAAAAASUVORK5CYII%3D

[badge support windows]: https://img.shields.io/badge/platform-windows-blue.svg?style=flat-square&logoWidth=20&logo=data%3Aimage%2Fpng%3Bbase64%2CiVBORw0KGgoAAAANSUhEUgAAABgAAAAYCAYAAADgdz34AAAABmJLR0QA%2FwD%2FAP%2BgvaeTAAAACXBIWXMAAA7EAAAOxAGVKw4bAAAAB3RJTUUH4AgSEisSipueyAAAAHBJREFUSMdjZKA2WPv%2BPzKXkSxDiuf%2FZ7AKIEopbgsW3v%2FPwCOA4AcLMqK7jhjAQo4mUgATA43BqAWjFlADiCvQ1HjsuXNJIwPD%2BgmMtLMAGyCzqBhNRaMWDAELWBiCBRmJrcDJy2hUaj1Q3wIiLQcAUjQgoD1kMJYAAAAASUVORK5CYII%3D

[badge sts]: https://img.shields.io/badge/-styczynsky_digital_systems-blue.svg?style=flat-square&logoWidth=20&logo=data%3Aimage%2Fpng%3Bbase64%2CiVBORw0KGgoAAAANSUhEUgAAABYAAAAXCAYAAAAP6L%2BeAAAABmJLR0QA%2FwD%2FAP%2BgvaeTAAAACXBIWXMAAA7DAAAOwwHHb6hkAAAAB3RJTUUH4AgSEh0nVTTLngAAAB1pVFh0Q29tbWVudAAAAAAAQ3JlYXRlZCB3aXRoIEdJTVBkLmUHAAAAm0lEQVQ4y2Pc%2Bkz2PwMNAAs2wVMzk4jSbJY%2BD6ccEwONACMsKIh1JSEgbXKeQdr4PO1cPPQMZiGkoC7bkCQD7%2Fx7znDn35AOClK9PEJSBbNYAJz999UGrOLocsM0KHB5EZ%2FXPxiVMDAwMDD8SP3DwJA6kFka5hJCQOBcDwMDAwPDm3%2FbGBj%2BbR8tNrFUTbiAB8tknHI7%2FuTilAMA9aAwA8miDpgAAAAASUVORK5CYII%3D

[screenshot 1]: https://raw.githubusercontent.com/styczynski/weatherly/master/static/screenshots/screenshot1.png
[screenshot 2]: https://raw.githubusercontent.com/styczynski/weatherly/master/static/screenshots/screenshot2.png

[logo]: https://raw.githubusercontent.com/styczynski/weatherly/master/src/main/resources/icons/weatherly32.png

[link isis97]: http://styczynski.ml
[link sts]: http://styczynski.ml
[link download latest]: https://github.com/styczynski/weatherly/archive/1.0.0.zip

VERSION=1.0.0

compile:
	mvn clean compile

test:
	mvn clean test

package:
	mvn clean package

build-docker-image:
	make package
	docker build -t iot_station_management:$(VERSION) .

package-build-docker-image:
	package build-docker-image

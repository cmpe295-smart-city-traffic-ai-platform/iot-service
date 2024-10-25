VERSION=1.0.0

compile:
	mvn clean compile

test:
	mvn clean test

package-jar:
	mvn clean package

build-docker-image:
	docker build -t iot_station_management:$(VERSION) --platform=linux/amd64 .

tag-docker-image-aws:
	docker tag iot_station_management:$(VERSION) 319282596033.dkr.ecr.us-east-1.amazonaws.com/smart_city_traffic/iot_station_management:$(VERSION)

push-docker-image-aws:
	docker push 319282596033.dkr.ecr.us-east-1.amazonaws.com/smart_city_traffic/iot_station_management:$(VERSION)

package-build-docker-image: package-jar build-docker-image

package-build-docker-image-aws: package-jar build-docker-image tag-docker-image-aws push-docker-image-aws

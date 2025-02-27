## 취미 기록 Web Page V3

취미 기록 Web Page V3
이 프로젝트는 개인의 취미를 기록하고 공유할 수 있는 웹사이트입니다. 
V3에서는 **N+1문제 해결 및 부하 테스트, 모니터링 시스템**을 도입했습니다.
사용자는 다양한 취미를 자유롭게 등록하고, 활동 내용을 정리하여 언제든지 돌아볼 수 있습니다. 
취미를 기록하는 즐거움을 통해 더 많은 사람들과 소통하고, 새로운 취미를 발견하는 기회를 가질 수 있습니다. 
나만의 취미 세상을 만들어보세요!


| **버전** | **링크**       |
|----------|----------------|
| 취미 기록 게시판 V1       | [취미 Web Page V1](https://github.com/kjyend/hobbyproject) |
| 취미 기록 게시판 V2       | [취미 Web Page V2](https://github.com/kjyend/hobbyprojectV2) |
| 취미 기록 게시판 V3       | [취미 Web Page V3](https://github.com/kjyend/hobbyprojectV3) |
| 취미 기록 게시판 V4       | [취미 Web Page V4](https://github.com/kjyend/hobbyprojectV4) |
| 취미 기록 게시판 V5       | [취미 Web Page V5](https://github.com/kjyend/hobbyprojectV5) |
## 개요

개발 기간 : 2024.9.26 ~ 2024.10.10

멤버 : 김준영(FE/BE)

## 시작 가이드

Installation
```
$ git clone https://github.com/kjyend/hobbyprojectV3.git
$ cd hobbyprojectV3
```
Backend
```
$ ./gradlew build
$ ./gradlew bootRun
```

## 기술 스택

Language (BE) : Java

Language (FE) : JavaScript

Library & Framework : Spring Boot, Spring, Thymeleaf, Hibernate 

Database : MySQL

ORM : JPA

Environment : IntelliJ, Git, GitHub 

Monitoring : Prometheus, Grafana

Performance Testing : JMeter

## V3 주요 변경 사항

✅ 댓글 조회 시 N+1 문제 발생 → Fetch Join으로 해결

* 댓글 조회 시 Member 엔티티를 개별적으로 불러오는 N+1 문제 발생
* Fetch Join을 사용하여 N+1 문제 해결 및 성능 최적화

✅ 게시글 조회 시 페이징 처리에서 N+1 문제 발생 → @BatchSize 적용으로 해결

* 페이징 처리 시 limit을 사용하면서 Fetch Join 적용 → Out Of Memory(OOM) 발생
* @BatchSize 적용을 통해 JPA의 In Query 방식으로 데이터 조회 최적화

✅ 시스템 모니터링 도입

* 문제 상황을 발생하기 위해서 Jmeter를 통해 부하 테스트 실행
* Prometheus, Grafana 도입으로 모니터링 시스템 구축 및 부하 테스트 데이터 수집


## ERD
![스크린샷 2024-10-07 210655](https://github.com/user-attachments/assets/e5b17d9e-5638-4934-81e2-500f615f5bfd)

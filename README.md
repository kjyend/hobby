## 취미 기록 Web Page V3

취미 기록 Web Page V3
이 프로젝트는 개인의 취미를 기록하고 공유할 수 있는 웹사이트입니다. 
V3에서는 N+1문제 해결 및 부하 테스트, 모니터링 시스템을 도입했습니다.
사용자는 다양한 취미를 자유롭게 등록하고, 활동 내용을 정리하여 언제든지 돌아볼 수 있습니다. 
취미를 기록하는 즐거움을 통해 더 많은 사람들과 소통하고, 새로운 취미를 발견하는 기회를 가질 수 있습니다. 
나만의 취미 세상을 만들어보세요!

## 개요

개발 기간 : 2024.9.26 ~ 2024.10.10

멤버 : 김준영(FE/BE)

## 시작 가이드

Installation
```
$ git clone https://github.com/kjyend/hobbyproject.git
$ cd hobbyproject
```
Backend
```
$ ./gradlew build
$ ./gradlew bootRun
```

## 기술 스택

Language (BE) : Java

Language (FE) : JavaScript, HTML, CSS

Library & Framework : Spring Boot, Spring, Thymeleaf, Hibernate 

Database : MySQL

ORM : JPA, Hibernate 

Environment : IntelliJ, Git, GitHub 

Monitoring : Prometheus, Grafana

Performance Testing : JMeter

## 트러블 슈팅

* 댓글 조회 시 발생하는 N+1 문제 발생 fetch join으로 문제 해결
* 게시글 조회 시 페이징 처리에서 발생하는 N+1 문제 발생, limit 메서드 사용으로 fetch join시 OOM이 발생 따라서 @BatchSize로 문제 해결


## ERD
![스크린샷 2024-10-07 210655](https://github.com/user-attachments/assets/e5b17d9e-5638-4934-81e2-500f615f5bfd)

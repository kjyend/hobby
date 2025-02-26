## 취미 기록 Web Page V5

취미 기록 Web Page V5
이 프로젝트는 개인의 취미를 기록하고 공유할 수 있는 웹사이트입니다. 
V5에서는 **좋아요 기능 최적화, 검색 기능 추가, 실시간 알림 시스템 도입**을 통해 사용자 경험을 더욱 향상했습니다.
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

개발 기간 : 2024.1.28 ~ 2024.2.23

멤버 : 김준영(FE/BE)

## 시작 가이드

Installation
```
$ git clone https://github.com/kjyend/hobbyprojectV5.git
$ cd hobbyprojectV5
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

Database : MySQL, Redis

ORM : JPA 

Environment : IntelliJ, Git, GitHub 

Monitoring : Prometheus, Grafana

Performance Testing : JMeter

## V5 주요 변경 사항
✅ 좋아요 기능 최적화

* 좋아요 갯수 추가
* 기존 DB 기반 좋아요 기능을 Redis 기반으로 개선하여 성능 최적화
* Redis에 저장된 데이터를 주기적으로 DB에 동기화하여 데이터 일관성 유지

✅ 검색 기능 추가

* 기본적인 키워드 검색 기능 구현

✅ 실시간 알림 시스템 구축

* Redis SSE를 활용한 실시간 알림 시스템 구현
* 사용자가 게시글에 좋아요를 받을 경우 실시간으로 알림 전송

✅ 페이징 UI/UX 변경 
* 숫자 페이지 버튼을 추가하여 1, 2, 3, 4처럼 직접 이동 가능하도록 변경

## 화면 구성
게시글 리스트 화면
![스크린샷 2025-02-26 154211](https://github.com/user-attachments/assets/4ff1a341-2297-4e42-b570-fe36f3927157)

로그인 후 게시글 리스트 화면
![스크린샷 2025-02-26 154516](https://github.com/user-attachments/assets/5f0edde1-ebe3-4fb1-9247-006989def51a)

검색 기능을 통한 결과 화면
![스크린샷 2025-02-26 160159](https://github.com/user-attachments/assets/76c97f8a-2f50-41b9-bf5c-d1910d687019)

좋아요 알림 기능 화면


https://github.com/user-attachments/assets/80118730-5c4e-4324-b1ac-1570a0098b02



## ERD
![화면 캡처 2025-01-27 204442](https://github.com/user-attachments/assets/2d2daaff-76cd-420e-9d34-2abfcb4ffdf3)

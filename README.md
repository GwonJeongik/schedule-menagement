|    기능    | HTTP Method |                         URL                         | request                                                                             |            response            |   상태 코드   |
|:--------:|:-----------:|:---------------------------------------------------:|-------------------------------------------------------------------------------------|:------------------------------:|:---------:|
|  일정 등록   |   `POST`    |                   /schedules/add                    | {<br/>"schedulePassword":"int",<br/>"task":"string",<br/>"adminName":"string"<br/>} |  등록 정보 <br/>application/json   | 200: 정상등록 |
|  일정 조회   |    `GET`    |              /schedules/{schedule_id}               | ID 경로 변수 @PathVariable                                                              | 단건 응답 정보 <br/>application/json | 200: 정상조회 |
| 일정 모두 조회 |    `GET`    | /schedules?modificationDate=string&adminName=string | 요청 param @ModelAttribute                                                            | 다건 응답 정보<br/>application/json  | 200: 정상조회 |
|  일정 수정   |   `PATCH`   |            /schedules/{scheduleId}/edit             | ID 경로 변수 @PathVariable, <br/>{<br/>"task":"string",<br/>"adminName":"string"<br/>}  |   수정 정보<br/>application/json   | 200: 정상수정 |
|  일정 삭제   |  `DELETE`   |           /schedules/{schedule_id}/delete           | ID 경로 변수 @PathVariable, <br/>{<br/>"schedulePassword":"int"<br/>}                   |               -                | 200: 정상삭제 |

![Copy of Schedule](https://github.com/user-attachments/assets/9948a4f0-284b-4918-b5ab-ed2f0504ecbc)

# Directory vessels (Справочник судов для СУДС)

Проектная работа для курса "Разработчик на Spring Framework"

### Description project (описание проекта)
Проект состоит из двух независимых модулей:
### 1. Модуль ***vessels_core***
Данный проект служит для получения, хранения, обновления и обработки данных о морских судах.
Оперативное пополнение данными АИС(через Kafka).
Возможность редактирования, удаления отдельных записей.

### 2.Модуль ***vessels_batch***
Служит для пакетной обработки csv файлов с данными о судах и дальнейшей загрузкой в базу данных.

### ***Build docker images***
Необходимо в корневой папке directory_vessels из терминала вызвать следующие комманды:
```
Docker build -t vessels_core_image -f Dockerfile_core .
Docker build -t vessels_batch_image -f Dockerfile_batch .
```

### ***Stack (используемые технологии)***
- Spring boot 2
- Spring Security
- Spring Data Jpa
- Spring Actuator
- Spring Validation
- Spring Cache (Caffeine)
- Spring Retry
- Spring Batch
- Java 11
- Mapstruct
- Apache Kafka
- Apache Avro
- Liquibase
- Spring doc open api
- Testcontainer
- Gradle
    
    



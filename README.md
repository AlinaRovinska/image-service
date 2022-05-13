Image service

Requirements

Implement standalone Spring boot application which will be responsible for uploading/search user images.

Domain

1 Account - user data, account can have multiple images.

2 Image - image metadata (original name, content type, size, reference, etc.), image can belong only to one account

3 Tag - list of string tags which can be used to search images, tags should not be duplicated in database

Use in memory H2 database.

Image entity should not contain binary content (value object) in database, but only image metadata.
Account and image entities should also contain creation and update timestamps.

API

1 CRUD API for accounts/images. API should accept multiple image files. Appropriate http status should be returned in case of non existing resource modification attempt.

2 Agile search API for images which allows search by any of image properties. API should return pageable result.

3 Account is allowed to modify only owned images.

Authentication

Use basic authentication for REST API.

Tests

Controller and service layer should be covered with tests.
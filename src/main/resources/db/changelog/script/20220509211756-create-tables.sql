drop table if exists accounts;

create table accounts
(
    id           integer auto_increment not null,
    account_name varchar(45)            not null,
    user_name    varchar(45)            not null,
    email        varchar(45)            not null,
    password     varchar(60)            not null,
    created_on   timestamp              not null,
    updated_on   timestamp              not null,

    primary key (id),
    unique (user_name)
);

drop table if exists images;

create table images
(
    id            integer auto_increment not null,
    original_name varchar(45)            not null,
    content_type  varchar(45)            not null,
    size          integer                not null,
    account_id    integer                not null,
    created_on    timestamp              not null,
    updated_on    timestamp              not null,

    primary key (id),

    constraint fk_account_id_image
        foreign key (account_id)
            references accounts (id)
);

drop table if exists tags;

create table tags
(
    id       integer auto_increment not null,
    tag_name varchar(45)            not null,

    unique (tag_name),

    primary key (id)
);

drop table if exists images_tags;

create table images_tags
(
    image_id integer not null,
    tag_id   integer not null,

    primary key (image_id, tag_id),

    constraint fk_image
        foreign key (image_id)
            references images (id),

    constraint fk_tag
        foreign key (tag_id)
            references tags (id)
);

drop table if exists roles;

create table roles
(
    id   integer auto_increment not null,
    role varchar(45)            not null,

    primary key (id),
    unique (role)
);

drop table if exists accounts_roles;

create table accounts_roles
(
    account_id integer not null,
    role_id    integer not null,

    primary key (account_id, role_id),

    constraint fk_account
        foreign key (account_id)
            references accounts (id),

    constraint fk_role
        foreign key (role_id)
            references roles (id)
);


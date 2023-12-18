-- Database: ServiceProDB

-- DROP DATABASE IF EXISTS "ServiceProDB";

CREATE DATABASE "ServiceProDB"
    WITH
    OWNER = postgres
    ENCODING = 'UTF8'
    LC_COLLATE = 'Portuguese_Brazil.1252'
    LC_CTYPE = 'Portuguese_Brazil.1252'
    TABLESPACE = pg_default
    CONNECTION LIMIT = -1;


    -- Table: public.auxiliar

    -- DROP TABLE IF EXISTS public.auxiliar;
 CREATE TABLE IF NOT EXISTS public.auxiliar
    (
        id bigint NOT NULL DEFAULT nextval('auxiliar_id_seq'::regclass),
        cpf character varying(255) COLLATE pg_catalog."default",
        datanascimento character varying(255) COLLATE pg_catalog."default",
        nome character varying(255) COLLATE pg_catalog."default",
        funcionario_id bigint,
        CONSTRAINT auxiliar_pkey PRIMARY KEY (id),
        CONSTRAINT uk_24el8xxheob55nh19b85613q5 UNIQUE (cpf),
        CONSTRAINT fkcyp38il29oh1x3fej8vc4vwfx FOREIGN KEY (funcionario_id)
            REFERENCES public.funcionario (id) MATCH SIMPLE
            ON UPDATE NO ACTION
            ON DELETE NO ACTION
    )

    TABLESPACE pg_default;

    ALTER TABLE IF EXISTS public.auxiliar
        OWNER to postgres;


 -- Table: public.funcionario

 -- DROP TABLE IF EXISTS public.funcionario;

 CREATE TABLE IF NOT EXISTS public.funcionario
 (
     id bigint NOT NULL DEFAULT nextval('funcionario_id_seq'::regclass),
     tipo character varying(255) COLLATE pg_catalog."default",
     cpf character varying(255) COLLATE pg_catalog."default",
     data character varying(255) COLLATE pg_catalog."default",
     email character varying(255) COLLATE pg_catalog."default",
     matricula character varying(255) COLLATE pg_catalog."default",
     nome character varying(255) COLLATE pg_catalog."default",
     CONSTRAINT funcionario_pkey PRIMARY KEY (id),
     CONSTRAINT uk_3uda6owswwy94ktwvq5uhifx1 UNIQUE (matricula),
     CONSTRAINT uk_rxosr8731eb3gbnlbt2mqfan8 UNIQUE (cpf)
 )

 TABLESPACE pg_default;

 ALTER TABLE IF EXISTS public.funcionario
     OWNER to postgres;

-- Table: public.ordem_de_servico

-- DROP TABLE IF EXISTS public.ordem_de_servico;

CREATE TABLE IF NOT EXISTS public.ordem_de_servico
(
    id bigint NOT NULL DEFAULT nextval('ordem_de_servico_id_seq'::regclass),
    dia_fechamento timestamp without time zone,
    matricula_funcionario character varying(255) COLLATE pg_catalog."default",
    nome_funcionario_responsavel character varying(255) COLLATE pg_catalog."default",
    requerente_matricula character varying(255) COLLATE pg_catalog."default",
    requerente_nome character varying(255) COLLATE pg_catalog."default",
    CONSTRAINT ordem_de_servico_pkey PRIMARY KEY (id)
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.ordem_de_servico
    OWNER to postgres;



-- Table: public.ordem_de_servico_projetor

-- DROP TABLE IF EXISTS public.ordem_de_servico_projetor;

CREATE TABLE IF NOT EXISTS public.ordem_de_servico_projetor
(
    id bigint NOT NULL DEFAULT nextval('ordem_de_servico_projetor_id_seq'::regclass),
    dia_fechamento timestamp without time zone,
    matricula_funcionario character varying(255) COLLATE pg_catalog."default",
    nome_funcionario_responsavel character varying(255) COLLATE pg_catalog."default",
    requerente_matricula character varying(255) COLLATE pg_catalog."default",
    requerente_nome character varying(255) COLLATE pg_catalog."default",
    CONSTRAINT ordem_de_servico_projetor_pkey PRIMARY KEY (id)
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.ordem_de_servico_projetor
    OWNER to postgres;


-- Table: public.ordem_de_servico_sala

-- DROP TABLE IF EXISTS public.ordem_de_servico_sala;

CREATE TABLE IF NOT EXISTS public.ordem_de_servico_sala
(
    id bigint NOT NULL DEFAULT nextval('ordem_de_servico_sala_id_seq'::regclass),
    dia_fechamento timestamp without time zone,
    matricula_funcionario character varying(255) COLLATE pg_catalog."default",
    nome_funcionario_responsavel character varying(255) COLLATE pg_catalog."default",
    requerente_matricula character varying(255) COLLATE pg_catalog."default",
    requerente_nome character varying(255) COLLATE pg_catalog."default",
    CONSTRAINT ordem_de_servico_sala_pkey PRIMARY KEY (id)
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.ordem_de_servico_sala
    OWNER to postgres;


-- Table: public.requerimento

-- DROP TABLE IF EXISTS public.requerimento;

CREATE TABLE IF NOT EXISTS public.requerimento
(
    codigo bigint NOT NULL DEFAULT nextval('requerimento_codigo_seq'::regclass),
    cpf character varying(255) COLLATE pg_catalog."default",
    data timestamp without time zone,
    descricao character varying(255) COLLATE pg_catalog."default",
    email character varying(255) COLLATE pg_catalog."default",
    matricula character varying(255) COLLATE pg_catalog."default",
    mensagem_retorno character varying(255) COLLATE pg_catalog."default",
    nome_requerente character varying(255) COLLATE pg_catalog."default",
    senha character varying(255) COLLATE pg_catalog."default",
    status character varying(255) COLLATE pg_catalog."default",
    funcionario_id bigint,
    CONSTRAINT requerimento_pkey PRIMARY KEY (codigo),
    CONSTRAINT uk_2hk3snc8pt608ib2qrf4tfsi UNIQUE (cpf),
    CONSTRAINT uk_cbhykbus34f612neoftlfvbmp UNIQUE (matricula),
    CONSTRAINT fkbaoi2htv0v92jyc5qgomrf84t FOREIGN KEY (funcionario_id)
        REFERENCES public.funcionario (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.requerimento
    OWNER to postgres;

-- Table: public.requerimento_projetor

-- DROP TABLE IF EXISTS public.requerimento_projetor;

CREATE TABLE IF NOT EXISTS public.requerimento_projetor
(
    codigo bigint NOT NULL DEFAULT nextval('requerimento_projetor_codigo_seq'::regclass),
    cpf character varying(255) COLLATE pg_catalog."default",
    data timestamp without time zone,
    data_de_uso character varying(255) COLLATE pg_catalog."default",
    descricao character varying(255) COLLATE pg_catalog."default",
    email character varying(255) COLLATE pg_catalog."default",
    hora_final character varying(255) COLLATE pg_catalog."default",
    hora_inicial character varying(255) COLLATE pg_catalog."default",
    matricula character varying(255) COLLATE pg_catalog."default",
    mensagem_retorno character varying(255) COLLATE pg_catalog."default",
    nome_requerente character varying(255) COLLATE pg_catalog."default",
    status character varying(255) COLLATE pg_catalog."default",
    funcionario_id bigint,
    CONSTRAINT requerimento_projetor_pkey PRIMARY KEY (codigo),
    CONSTRAINT uk_8pb7d5rt7hbn09957jfd3udu1 UNIQUE (matricula),
    CONSTRAINT uk_tfa94pj3yo64jk1rd49t3763 UNIQUE (cpf),
    CONSTRAINT fkn9fv7sye6mondobrslk0i4vxg FOREIGN KEY (funcionario_id)
        REFERENCES public.funcionario (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.requerimento_projetor
    OWNER to postgres;

-- Table: public.requerimento_sala

-- DROP TABLE IF EXISTS public.requerimento_sala;

CREATE TABLE IF NOT EXISTS public.requerimento_sala
(
    codigo bigint NOT NULL DEFAULT nextval('requerimento_sala_codigo_seq'::regclass),
    cpf character varying(255) COLLATE pg_catalog."default",
    data timestamp without time zone,
    data_de_uso character varying(255) COLLATE pg_catalog."default",
    descricao character varying(255) COLLATE pg_catalog."default",
    email character varying(255) COLLATE pg_catalog."default",
    hora_final character varying(255) COLLATE pg_catalog."default",
    hora_inicial character varying(255) COLLATE pg_catalog."default",
    matricula character varying(255) COLLATE pg_catalog."default",
    mensagem_retorno character varying(255) COLLATE pg_catalog."default",
    nome_requerente character varying(255) COLLATE pg_catalog."default",
    numero_sala character varying(255) COLLATE pg_catalog."default",
    status character varying(255) COLLATE pg_catalog."default",
    funcionario_id bigint,
    CONSTRAINT requerimento_sala_pkey PRIMARY KEY (codigo),
    CONSTRAINT uk_8by1vpj69kndjhye17ly1glqg UNIQUE (matricula),
    CONSTRAINT uk_y9vjx5mjs6ugb0yspgmlan5n UNIQUE (cpf),
    CONSTRAINT fkclxgcee4gkfe5pgdtkjupinla FOREIGN KEY (funcionario_id)
        REFERENCES public.funcionario (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.requerimento_sala
    OWNER to postgres;
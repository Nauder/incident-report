CREATE TABLE IF NOT EXISTS public.user (
    id_user serial PRIMARY KEY,
    nome character varying NOT NULL,
    email character varying NOT NULL,
    senha character varying NOT NULL
);

CREATE TABLE IF NOT EXISTS public.incidente (
    id_incidente serial PRIMARY KEY,
    id_user integer NOT NULL,
    data character varying NOT NULL,
    hora character varying NOT NULL,
    estado character(2) NOT NULL,
    cidade character(50) NOT NULL,
    bairro character(50) NOT NULL,
    rua character(50) NOT NULL,
    tipo_incidente integer NOT NULL
);

ALTER TABLE public.incidente
ADD CONSTRAINT fk_user_incidente
FOREIGN KEY(id_user)
REFERENCES public.user(id_user)
ON DELETE SET NULL
ON UPDATE CASCADE;

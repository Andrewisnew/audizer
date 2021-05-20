CREATE TABLE IF NOT EXISTS public.users
(
    id bigint NOT NULL,
    messenger "char"[] NOT NULL,
    messenger_user_id bigint NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE public.messages
(
    id bigint NOT NULL,
    message_id bigint NOT NULL,
    text "char"[] NOT NULL,
    user_id bigint NOT NULL,
    PRIMARY KEY (id)
);
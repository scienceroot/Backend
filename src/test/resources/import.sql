INSERT INTO interests(id, name) VALUES (uuid_in(md5(random()::text || now()::text)::cstring), 'Acoustics');

INSERT INTO scr_languages(id, name) VALUES (uuid_in(md5(random()::text || now()::text)::cstring), 'Afrikaans');
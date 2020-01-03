INSERT INTO public.companies (id, mark_avg, description, name, type, owner_id, blocked, photo_url) VALUES (3, 3, '', 'Polska', 'EMPLOYEE', 14, false, NULL);
INSERT INTO public.companies (id, mark_avg, description, name, type, owner_id, blocked, photo_url) VALUES (2, 5, '', 'Emploee', 'EMPLOYEE', 13, false, NULL);
INSERT INTO public.companies (id, mark_avg, description, name, type, owner_id, blocked, photo_url)VALUES (1, 4, 'Goooooooood', 'Storona', 'EMPLOYER', 3, false, NULL);

INSERT INTO public.profile (id, accepted, first_name, is_root, last_name, nickname, photo_url, company_id, user_id) VALUES (3, true, 'Dmytro', NULL, 'Stepanyk', 'doromador', '', 1, 3);
INSERT INTO public.profile (id, accepted, first_name, is_root, last_name, nickname, photo_url, company_id, user_id) VALUES (14, true, 'Stepa', NULL, 'Savka', 'sava', '', 1, 14);
INSERT INTO public.profile (id, accepted, first_name, is_root, last_name, nickname, photo_url, company_id, user_id) VALUES (13, true, 'Mike', NULL, 'Palahuta', 'palah', '', 1, 13);


INSERT INTO public.project (id, budget, description, employeemark, employermark, expirydate, name, photo_url, status, creator_id, employee_id, type_id) VALUES (26, 2000000, 'Good project', NULL, NULL, '2021-02-26', 'Lacalut', NULL, 'NEW', 1, NULL, 10);
INSERT INTO public.project (id, budget, description, employeemark, employermark, expirydate, name, photo_url, status, creator_id, employee_id, type_id) VALUES (35, 200000, 'Good project', NULL, NULL, '2021-02-11', 'IDUkrain', NULL, 'FAILED', 1, NULL, 10);
INSERT INTO public.project (id, budget, description, employeemark, employermark, expirydate, name, photo_url, status, creator_id, employee_id, type_id) VALUES (39, 432400, 'Your hair will be too amazing', NULL, NULL, '2020-02-29', 'Head&Sholders', NULL, 'NEW', 1, NULL, 10);
INSERT INTO public.project (id, budget, description, employeemark, employermark, expirydate, name, photo_url, status, creator_id, employee_id, type_id) VALUES (40, 432400, 'dgfghdhfsfgsgf tsryhrtsysryhrt', NULL, NULL, '2019-08-31', 'Adddd', NULL, 'NEW', 1, NULL, 10);
INSERT INTO public.project (id, budget, description, employeemark, employermark, expirydate, name, photo_url, status, creator_id, employee_id, type_id) VALUES (41, 345643, 'bbbbbbbbbbbbbbbbbbbbb', NULL, NULL, '2019-08-30', 'fjhgfty', NULL, 'NEW', 1, NULL, 10);
INSERT INTO public.project (id, budget, description, employeemark, employermark, expirydate, name, photo_url, status, creator_id, employee_id, type_id) VALUES (32, 2000000, 'Good project', NULL, NULL, '2021-02-26', 'Amazon', NULL, 'COMPLETED', 1, 2, 10);
INSERT INTO public.project (id, budget, description, employeemark, employermark, expirydate, name, photo_url, status, creator_id, employee_id, type_id) VALUES (29, 2000000, 'Good project', NULL, NULL, '2021-02-26', 'Bomba', NULL, 'FAILED', 1, NULL, 10);

INSERT INTO public.project_type (id, name) VALUES (10, 'Java');

INSERT INTO public.projectrequest (id, employee_id, employer_id, project_id) VALUES (2, 2, 1, 32);

INSERT INTO public.required_skills (project_id, skill_id) VALUES (39, 2);
INSERT INTO public.required_skills (project_id, skill_id) VALUES (40, 2);
INSERT INTO public.required_skills (project_id, skill_id) VALUES (41, 2);


INSERT INTO public.skills (id, name) VALUES (1, 'desfs');
INSERT INTO public.skills (id, name) VALUES (2, 'C++');



INSERT INTO public.user_info (id, is_active, password, username) VALUES (1, false, '$2a$10$MZ29ZamSVrPfs4h5oGjBT.JUPmb5/EV6u5I8emSu0hSX4rnLqI/Se', 'dstepanuk0@gmail.c0m');
INSERT INTO public.user_info (id, is_active, password, username) VALUES (2, true, '$2a$10$6lsNzPWDK4J8J2f9IBCJQOVSi53riPQDPPLGXh6Z3skVV8HgjYWN6', 'dstepanuk0@gmail.com');
INSERT INTO public.user_info (id, is_active, password, username) VALUES (3, true, '$2a$10$WEBoWVlOl74k34xQUveCk.G9lwlkuif9IHDgSCxB89we76eLH3zdC', 'stepanyk@gmail.com');
INSERT INTO public.user_info (id, is_active, password, username) VALUES (13, true, '$2a$10$buFjltxQ34xpamsHnAbZReqGckKUYDuc8GsNbdiSZgmIJ.ylqyf2u', 'palahuta@gmail.com');
INSERT INTO public.user_info (id, is_active, password, username) VALUES (14, true, '$2a$10$FIK/RfosPNi/Sgi8j/SRqOr032ddm.PVL0JF8e8uToxBfgsrUflgW', 'stepa@gmail.com');


INSERT INTO public.user_recommendations (id, valuation, profile_id, project_type_id) VALUES (38, 7, 3, 10);



INSERT INTO public.user_role (user_id, roles) VALUES (1, 'USER');
INSERT INTO public.user_role (user_id, roles) VALUES (2, 'USER');
INSERT INTO public.user_role (user_id, roles) VALUES (3, 'USER');
INSERT INTO public.user_role (user_id, roles) VALUES (13, 'USER');
INSERT INTO public.user_role (user_id, roles) VALUES (14, 'USER');


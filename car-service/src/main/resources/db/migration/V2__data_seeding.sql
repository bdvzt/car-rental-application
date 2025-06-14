-- модели машин
INSERT INTO car_models (id, brand, model, year, color, created_at, created_by)
VALUES
    ('b9904c45-3084-4775-afc9-9ea6dfc03350', 'Toyota', 'Camry', 2020, 'Черный', now(), 'ebc0465f-4f19-427d-bb8b-7c72874fe62e'),
    ('6d45a4eb-724a-4521-bc76-ec7be92ee7fd', 'BMW', 'X5', 2019, 'Серебристый', now(), 'ebc0465f-4f19-427d-bb8b-7c72874fe62e'),
    ('5a854216-d509-4996-8300-6ac8934c7ea0', 'BMW', 'X6', 2020, 'Белый', now(), 'ebc0465f-4f19-427d-bb8b-7c72874fe62e'),
    ('2cff69fb-79cc-4a9b-bad4-73c15caefbba', 'Mercedes', 'GLE', 2021, 'Черный', now(), 'ebc0465f-4f19-427d-bb8b-7c72874fe62e'),
    ('51048f97-d0ce-402d-93d1-0de65a4bd6f1', 'Hyundai', 'Solaris', 2018, 'Красный', now(), 'ebc0465f-4f19-427d-bb8b-7c72874fe62e'),
    ('e1a7f20d-188f-4d1e-818f-fccf99c1c20d', 'Lada', 'Vesta', 2021, 'Синий', now(), 'ebc0465f-4f19-427d-bb8b-7c72874fe62e');

-- машины (регион 03 йооооу)
INSERT INTO cars (id, car_number, car_model_id, price_per_day, description, status, created_at, created_by)
VALUES
    ('a1b2c3d4-e5f6-4781-a1a1-a1a1a1a1a1a1', 'А777АА03', 'b9904c45-3084-4775-afc9-9ea6dfc03350', 3000.00, 'Комфортный седан', 'AVAILABLE', now(), 'ebc0465f-4f19-427d-bb8b-7c72874fe62e'),
    ('b2c3d4e5-f6a7-4892-b2b2-b2b2b2b2b2b2', 'А666АА03', '6d45a4eb-724a-4521-bc76-ec7be92ee7fd', 5000.00, 'Полный привод, люкс', 'AVAILABLE', now(), 'ebc0465f-4f19-427d-bb8b-7c72874fe62e'),
    ('c3d4e5f6-a7b8-4903-c3c3-c3c3c3c3c3c3', 'А665АА03', '6d45a4eb-724a-4521-bc76-ec7be92ee7fd', 4900.00, 'Немного дешевле, но всё ещё люкс', 'AVAILABLE', now(), 'ebc0465f-4f19-427d-bb8b-7c72874fe62e'),
    ('d4e5f6a7-b8c9-4014-d4d4-d4d4d4d4d4d4', 'А664АА03', '5a854216-d509-4996-8300-6ac8934c7ea0', 5200.00, 'BMW X6 белая, премиум комплектация', 'AVAILABLE', now(), 'ebc0465f-4f19-427d-bb8b-7c72874fe62e'),
    ('e5f6a7b8-c9d0-4125-e5e5-e5e5e5e5e5e5', 'А663АА03', '2cff69fb-79cc-4a9b-bad4-73c15caefbba', 6100.00, 'Mercedes черный с панорамой', 'AVAILABLE', now(), 'ebc0465f-4f19-427d-bb8b-7c72874fe62e'),
    ('f6a7b8c9-d0e1-4236-f6f6-f6f6f6f6f6f6', 'А662АА03', '51048f97-d0ce-402d-93d1-0de65a4bd6f1', 2100.00, 'Hyundai Solaris для городских поездок', 'UNDER_REPAIR', now(), 'ebc0465f-4f19-427d-bb8b-7c72874fe62e'),
    ('a7b8c9d0-e1f2-4347-a7a7-a7a7a7a7a7a7', 'А003АА03', 'e1a7f20d-188f-4d1e-818f-fccf99c1c20d', 1500.00, 'Lada Vesta, стандарт', 'AVAILABLE', now(), 'ebc0465f-4f19-427d-bb8b-7c72874fe62e'),
    ('b8c9d0e1-f2a3-4458-b8b8-b8b8b8b8b8b8', 'А002АА03', 'e1a7f20d-188f-4d1e-818f-fccf99c1c20d', 1400.00, 'Lada Vesta, немного подержанная', 'AVAILABLE', now(), 'ebc0465f-4f19-427d-bb8b-7c72874fe62e'),
    ('c9d0e1f2-a3b4-4569-c9c9-c9c9c9c9c9c9', 'А001АА03', 'e1a7f20d-188f-4d1e-818f-fccf99c1c20d', 1350.00, 'Lada Vesta для начинающих', 'UNDER_REPAIR', now(), 'ebc0465f-4f19-427d-bb8b-7c72874fe62e');
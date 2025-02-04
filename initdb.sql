-- create database online_course;

-- use online_course;

-- Insaccount_profileert roles into the 'role' table
INSERT INTO roles (name) VALUES ('ADMIN'),
                                ('INSTRUCTOR'),
                                ('LEARNER');

-- Insert one account into the 'account' table password_hash: 12345
INSERT INTO accounts (email, password_hash)
VALUES ('admin@gmail.com', '5994471abb01112afcc18159f6cc74b4f511b99806da59b3caf5a9c173cacfc5');


INSERT INTO accounts (email, password_hash)
VALUES ('instuctor@gmail.com', '5994471abb01112afcc18159f6cc74b4f511b99806da59b3caf5a9c173cacfc5');

INSERT INTO accounts (email, password_hash)
VALUES ('learner@gmail.com', '5994471abb01112afcc18159f6cc74b4f511b99806da59b3caf5a9c173cacfc5');

INSERT INTO account_roles (account_id, role_id)
VALUES (1, 1);

INSERT INTO account_roles (account_id, role_id)
VALUES (2, 2);

INSERT INTO account_roles (account_id, role_id)
VALUES (3, 3);

INSERT INTO permissions (name)
VALUES
    ('UPLOAD_FILE'),
    ('DOWNLOAD_FILE'),
    ('CREATE_BLOG'),
    ('UPDATE_BLOG'),
    ('DELETE_BLOG'),
    ('REACT_BLOG'),
    ('CREATE_COMMENT_BLOG'),
    ('UPDATE_COMMENT_BLOG'),
    ('DELETE_COMMENT_BLOG'),
    ('ANSWER_COMMENT_BLOG'),
    ('BOOKMARK_BLOG');

INSERT INTO role_permission (role_id, permission_id)
VALUES (1, 1);

INSERT INTO role_permission (role_id, permission_id)
VALUES (1, 3);

INSERT INTO role_permission (role_id, permission_id)
VALUES (2, 3);

INSERT INTO account_profile (id, first_name, last_name, created_at, updated_at)
VALUES
    (1, 'admin', 'nguyen', NOW(), NOW()),
    (2, 'instructor', 'nguyen', NOW(), NOW()),
    (3, 'learner', 'nguyen', NOW(), NOW());


DELIMITER $$

CREATE FUNCTION deAccent(str TEXT) RETURNS TEXT
    DETERMINISTIC
BEGIN
    -- Trim spaces and replace multiple spaces with a single space
    SET str = TRIM(str);
    SET str = REGEXP_REPLACE(str, ' +', ' ');

    -- Replace accented characters with non-accented equivalents
    SET str = REPLACE(str, 'à', 'a');
    SET str = REPLACE(str, 'á', 'a');
    SET str = REPLACE(str, 'ạ', 'a');
    SET str = REPLACE(str, 'ả', 'a');
    SET str = REPLACE(str, 'ã', 'a');
    SET str = REPLACE(str, 'â', 'a');
    SET str = REPLACE(str, 'ầ', 'a');
    SET str = REPLACE(str, 'ấ', 'a');
    SET str = REPLACE(str, 'ậ', 'a');
    SET str = REPLACE(str, 'ẩ', 'a');
    SET str = REPLACE(str, 'ẫ', 'a');
    SET str = REPLACE(str, 'ă', 'a');
    SET str = REPLACE(str, 'ằ', 'a');
    SET str = REPLACE(str, 'ắ', 'a');
    SET str = REPLACE(str, 'ặ', 'a');
    SET str = REPLACE(str, 'ẳ', 'a');
    SET str = REPLACE(str, 'ẵ', 'a');

    SET str = REPLACE(str, 'À', 'A');
    SET str = REPLACE(str, 'Á', 'A');
    SET str = REPLACE(str, 'Ạ', 'A');
    SET str = REPLACE(str, 'Ả', 'A');
    SET str = REPLACE(str, 'Ã', 'A');
    SET str = REPLACE(str, 'Â', 'A');
    SET str = REPLACE(str, 'Ầ', 'A');
    SET str = REPLACE(str, 'Ấ', 'A');
    SET str = REPLACE(str, 'Ậ', 'A');
    SET str = REPLACE(str, 'Ẩ', 'A');
    SET str = REPLACE(str, 'Ẫ', 'A');
    SET str = REPLACE(str, 'Ă', 'A');
    SET str = REPLACE(str, 'Ằ', 'A');
    SET str = REPLACE(str, 'Ắ', 'A');
    SET str = REPLACE(str, 'Ặ', 'A');
    SET str = REPLACE(str, 'Ẳ', 'A');
    SET str = REPLACE(str, 'Ẵ', 'A');

    SET str = REPLACE(str, 'è', 'e');
    SET str = REPLACE(str, 'é', 'e');
    SET str = REPLACE(str, 'ẹ', 'e');
    SET str = REPLACE(str, 'ẻ', 'e');
    SET str = REPLACE(str, 'ẽ', 'e');
    SET str = REPLACE(str, 'ê', 'e');
    SET str = REPLACE(str, 'ề', 'e');
    SET str = REPLACE(str, 'ế', 'e');
    SET str = REPLACE(str, 'ệ', 'e');
    SET str = REPLACE(str, 'ể', 'e');
    SET str = REPLACE(str, 'ễ', 'e');

    SET str = REPLACE(str, 'È', 'E');
    SET str = REPLACE(str, 'É', 'E');
    SET str = REPLACE(str, 'Ẹ', 'E');
    SET str = REPLACE(str, 'Ẻ', 'E');
    SET str = REPLACE(str, 'Ẽ', 'E');
    SET str = REPLACE(str, 'Ê', 'E');
    SET str = REPLACE(str, 'Ề', 'E');
    SET str = REPLACE(str, 'Ế', 'E');
    SET str = REPLACE(str, 'Ệ', 'E');
    SET str = REPLACE(str, 'Ể', 'E');
    SET str = REPLACE(str, 'Ễ', 'E');

    SET str = REPLACE(str, 'ì', 'i');
    SET str = REPLACE(str, 'í', 'i');
    SET str = REPLACE(str, 'ị', 'i');
    SET str = REPLACE(str, 'ỉ', 'i');
    SET str = REPLACE(str, 'ĩ', 'i');

    SET str = REPLACE(str, 'Ì', 'I');
    SET str = REPLACE(str, 'Í', 'I');
    SET str = REPLACE(str, 'Ị', 'I');
    SET str = REPLACE(str, 'Ỉ', 'I');
    SET str = REPLACE(str, 'Ĩ', 'I');

    SET str = REPLACE(str, 'ò', 'o');
    SET str = REPLACE(str, 'ó', 'o');
    SET str = REPLACE(str, 'ọ', 'o');
    SET str = REPLACE(str, 'ỏ', 'o');
    SET str = REPLACE(str, 'õ', 'o');
    SET str = REPLACE(str, 'ô', 'o');
    SET str = REPLACE(str, 'ồ', 'o');
    SET str = REPLACE(str, 'ố', 'o');
    SET str = REPLACE(str, 'ộ', 'o');
    SET str = REPLACE(str, 'ổ', 'o');
    SET str = REPLACE(str, 'ỗ', 'o');
    SET str = REPLACE(str, 'ơ', 'o');
    SET str = REPLACE(str, 'ờ', 'o');
    SET str = REPLACE(str, 'ớ', 'o');
    SET str = REPLACE(str, 'ợ', 'o');
    SET str = REPLACE(str, 'ở', 'o');
    SET str = REPLACE(str, 'ỡ', 'o');

    SET str = REPLACE(str, 'Ò', 'O');
    SET str = REPLACE(str, 'Ó', 'O');
    SET str = REPLACE(str, 'Ọ', 'O');
    SET str = REPLACE(str, 'Ỏ', 'O');
    SET str = REPLACE(str, 'Õ', 'O');
    SET str = REPLACE(str, 'Ô', 'O');
    SET str = REPLACE(str, 'Ồ', 'O');
    SET str = REPLACE(str, 'Ố', 'O');
    SET str = REPLACE(str, 'Ộ', 'O');
    SET str = REPLACE(str, 'Ổ', 'O');
    SET str = REPLACE(str, 'Ỗ', 'O');
    SET str = REPLACE(str, 'Ơ', 'O');
    SET str = REPLACE(str, 'Ờ', 'O');
    SET str = REPLACE(str, 'Ớ', 'O');
    SET str = REPLACE(str, 'Ợ', 'O');
    SET str = REPLACE(str, 'Ở', 'O');
    SET str = REPLACE(str, 'Ỡ', 'O');

    SET str = REPLACE(str, 'đ', 'd');
    SET str = REPLACE(str, 'Đ', 'D');

RETURN LOWER(str);
END $$

DELIMITER ;

INSERT INTO blogs (title, slug, content, create_by, account_id, create_at)
VALUES
    ('Tương lai của Công nghệ: Các xu hướng cần chú ý', 'tuong-lai-cua-cong-nghe-cac-xu-huong-can-chu-y',
     '<p>Ngành công nghệ đang phát triển nhanh hơn bao giờ hết, và có nhiều xu hướng dự kiến sẽ thay đổi cuộc chơi. Từ những tiến bộ trong AI đến các ứng dụng blockchain, bài viết này khám phá những sự thay đổi lớn mà chúng ta có thể mong đợi trong những năm tới.</p>',
     'admin', 1, NOW()),

    ('5 Lời khuyên để duy trì sự cân bằng công việc và cuộc sống', '5-loi-khuyen-de-duy-tri-su-can-bang-cong-viec-va-cuoc-song',
     '<p>Trong thế giới ngày nay với nhịp độ nhanh, việc duy trì sự cân bằng giữa sự nghiệp và cuộc sống cá nhân có thể là điều khó khăn. Bài viết này cung cấp những lời khuyên thực tế về cách đạt được sự cân bằng công việc-cuộc sống khỏe mạnh hơn.</p>',
     'admin', 2, NOW()),

    ('Top 10 điểm đến cần tham quan vào năm 2025', 'top-10-diem-den-can-tham-quan-vao-nam-2025',
     '<p>Nếu bạn đang có kế hoạch du lịch trong năm 2025, đây là top 10 điểm đến mà bạn nên xem xét. Từ những bãi biển tuyệt đẹp đến các công trình lịch sử, những địa điểm này là lý tưởng cho bất kỳ du khách nào.</p>',
     'admin', 2, NOW()),

    ('AI đang thay đổi ngành y tế như thế nào', 'ai-dang-thay-doi-nganh-y-te-nhu-the-nao',
     '<p>AI có tiềm năng cách mạng hóa ngành y tế, từ việc chẩn đoán bệnh đến việc tạo ra các kế hoạch điều trị cá nhân hóa. Bài viết này khám phá cách trí tuệ nhân tạo đang tái định hình tương lai của y học.</p>',
     'admin', 1, NOW()),

    ('Hiểu về công nghệ Blockchain', 'hieu-ve-cong-nghe-blockchain',
     '<p>Blockchain không chỉ dành cho tiền điện tử. Trong bài viết này, chúng tôi khám phá cách công nghệ blockchain hoạt động và cách nó đang thay đổi các ngành công nghiệp, từ ngân hàng đến quản lý chuỗi cung ứng.</p>',
     'admin', 2, NOW()),

    ('Hướng dẫn toàn diện về tiếp thị số vào năm 2025', 'huong-dan-toan-dien-ve-tiep-thi-so-vao-nam-2025',
     '<p>Tiếp thị số đang thay đổi liên tục. Hướng dẫn toàn diện này bao gồm các xu hướng, công cụ và kỹ thuật mới nhất trong tiếp thị số để giúp các doanh nghiệp đi trước một bước vào năm 2025.</p>',
     'admin', 2, NOW()),

    ('10 cách để cải thiện năng suất cá nhân của bạn', '10-cach-de-cai-thien-nang-suat-ca-nhan-cua-ban',
     '<p>Những mẹo năng suất có thể giúp bạn quản lý thời gian và tập trung tốt hơn. Dưới đây là 10 cách thực tế để tăng cường năng suất cá nhân của bạn mỗi ngày.</p>',
     'admin', 2, NOW()),

    ('Cách xây dựng một sự hiện diện mạnh mẽ trên mạng cho thương hiệu của bạn', 'cach-xay-dung-mot-su-hien-dien-manh-me-tren-mang-cho-thuong-hieu-cua-ban',
     '<p>Xây dựng một sự hiện diện mạnh mẽ trên mạng là điều cần thiết cho bất kỳ doanh nghiệp nào. Bài viết này giải thích các bước bạn có thể thực hiện để củng cố sự nhận diện và khả năng hiển thị thương hiệu của mình trên mạng.</p>',
     'admin', 2, NOW()),

    ('Tầm quan trọng của sức khỏe tâm thần trong công sở', 'tam-quan-trong-cua-suc-khoe-tam-than-trong-cong-so',
     '<p>Hỗ trợ sức khỏe tâm thần trong công sở có thể dẫn đến năng suất và sự hài lòng cao hơn từ nhân viên. Bài viết này thảo luận về lý do tại sao sức khỏe tâm thần quan trọng trong môi trường công sở và cách các doanh nghiệp có thể hỗ trợ nhân viên của mình.</p>',
     'admin', 2, NOW()),

    ('Thực hành tốt nhất trong việc quản lý các đội nhóm làm việc từ xa', 'thuc-hanh-tot-nhat-trong-viec-quan-ly-cac-doi-nhom-lam-viec-tu-xa',
     '<p>Quản lý các đội nhóm làm việc từ xa đi kèm với những thử thách riêng. Bài viết này cung cấp các mẹo thực tiễn và thực hành tốt nhất để lãnh đạo các đội nhóm làm việc từ xa một cách hiệu quả và nâng cao năng suất của họ.</p>',
     'admin', 2, NOW()),

    ('Cách tận dụng mạng xã hội để phát triển kinh doanh', 'cach-tan-dung-mang-xa-hoi-de-phat-trien-kinh-doanh',
     '<p>Mạng xã hội là một công cụ mạnh mẽ để phát triển doanh nghiệp của bạn. Bài viết này khám phá cách tận dụng các nền tảng khác nhau để tiếp cận khán giả mới và phát triển thương hiệu của bạn.</p>',
     'admin', 2, NOW()),

    ('Khám phá tương lai của phương tiện điện tử', 'kham-pha-tuong-lai-cua-phuong-tien-dien-tu',
     '<p>Thị trường phương tiện điện tử đang phát triển mạnh mẽ. Trong bài viết này, chúng tôi nhìn nhận sâu hơn về các đổi mới, thách thức và cơ hội đang hình thành tương lai của phương tiện điện tử.</p>',
     'admin', 1, NOW()),

    ('Cách tạo ra một chiến lược tiếp thị nội dung hiệu quả', 'cach-tao-ra-mot-chien-luoc-tiep-thi-noi-dung-hieu-qua',
     '<p>Tiếp thị nội dung là một trong những chiến lược mạnh mẽ nhất để thu hút khán giả. Bài viết này phác thảo các bước để tạo ra một chiến lược tiếp thị nội dung giúp đạt được kết quả.</p>',
     'admin', 2, NOW()),

    ('Sự trỗi dậy của làm việc từ xa và ảnh hưởng của nó đối với lực lượng lao động toàn cầu', 'su-troi-day-cua-lam-viec-tu-xa-va-anh-huong-cua-no-doi-voi-luc-luong-lao-dong-toan-cau',
     '<p>Làm việc từ xa đã trở thành một xu hướng phổ biến đối với nhiều công ty. Bài viết này khám phá cách làm việc từ xa ảnh hưởng đến lực lượng lao động toàn cầu và những gì điều đó có thể có ý nghĩa đối với tương lai của công việc.</p>',
     'admin', 2, NOW()),

    ('Tại sao sự bền vững lại quan trọng đối với tương lai của doanh nghiệp', 'tai-sao-su-ben-vung-lai-quan-trong-doi-voi-tuong-lai-cua-doanh-nghiep',
     '<p>Sự bền vững không còn là một thuật ngữ thịnh hành; nó là một yêu cầu đối với doanh nghiệp. Trong bài viết này, chúng tôi khám phá lý do tại sao sự bền vững quan trọng đối với doanh nghiệp và cách nó có thể giúp đạt được thành công lâu dài.</p>',
     'admin', 1, NOW()),

    ('Khám phá tác động của công nghệ 5G đối với doanh nghiệp', 'kham-pha-tac-dong-cua-cong-nghe-5g-doi-voi-doanh-nghiep',
     '<p>Công nghệ 5G sẽ cách mạng hóa các ngành công nghiệp trên toàn cầu. Bài viết này thảo luận về cách doanh nghiệp có thể tận dụng công nghệ 5G để cải thiện hoạt động và trải nghiệm khách hàng.</p>',
     'admin', 2, NOW()),

    ('Lợi ích của việc triển khai trí tuệ nhân tạo trong doanh nghiệp của bạn', 'loi-ich-cua-viec-trien-khai-tri-tue-nhan-tao-trong-doanh-nghiep-cua-ban',
     '<p>Trí tuệ nhân tạo có thể mang lại nhiều lợi ích cho doanh nghiệp. Từ việc tự động hóa các nhiệm vụ đến cải thiện quyết định, bài viết này khám phá cách AI có thể giúp bạn phát triển doanh nghiệp của mình.</p>',
     'admin', 1, NOW()),

    ('Hướng dẫn cho người mới bắt đầu về đầu tư tiền điện tử', 'huong-dan-cho-nguoi-moi-bat-dau-ve-dau-tu-tien-dien-tu',
     '<p>Đầu tư tiền điện tử có thể gây khó khăn cho những người mới bắt đầu. Bài viết này cung cấp một hướng dẫn toàn diện cho những ai muốn tìm hiểu về cách đầu tư vào các loại tiền điện tử như Bitcoin và Ethereum.</p>',
     'admin', 2, NOW()),

    ('Sự phát triển của thương mại điện tử và xu hướng mới nhất', 'su-phat-trien-cua-thuong-mai-dien-tu-va-xu-huong-moi-nhat',
     '<p>Thương mại điện tử đang phát triển nhanh chóng và có nhiều xu hướng mới đang hình thành. Bài viết này thảo luận về những thay đổi trong ngành thương mại điện tử và các chiến lược mà các doanh nghiệp có thể áp dụng để đi đầu trong cuộc đua này.</p>',
     'admin', 1, NOW());
INSERT INTO blogs (title, slug, content, create_by, account_id, create_at)
VALUES
    ('Chìa khóa để tăng trưởng doanh thu trong năm 2025', 'chia-khoa-de-tang-truong-doanh-thu-trong-nam-2025',
     '<p>Để doanh nghiệp phát triển bền vững, tăng trưởng doanh thu là một yếu tố quan trọng. Bài viết này chia sẻ các chiến lược giúp bạn tăng trưởng doanh thu một cách hiệu quả trong năm 2025.</p>',
     'admin', 1, NOW()),

    ('Phân tích các xu hướng thiết kế web hiện đại', 'phan-tich-cac-xu-huong-thiet-ke-web-hien-dai',
     '<p>Thiết kế web đã thay đổi rất nhiều trong những năm qua. Bài viết này sẽ giúp bạn hiểu rõ hơn về các xu hướng thiết kế web mới nhất và cách áp dụng chúng vào các dự án của bạn.</p>',
     'admin', 2, NOW()),

    ('Cách xây dựng một hệ sinh thái thương mại điện tử thành công', 'cach-xay-dung-mot-he-sinh-thai-thuong-mai-dien-tu-thanh-cong',
     '<p>Thương mại điện tử là một ngành đang phát triển mạnh mẽ. Bài viết này chia sẻ các chiến lược để xây dựng một hệ sinh thái thương mại điện tử thành công và bền vững.</p>',
     'admin', 1, NOW()),

    ('Tại sao digital marketing lại quan trọng trong việc xây dựng thương hiệu', 'tai-sao-digital-marketing-lai-quan-trong-trong-viec-xay-dung-thuong-hieu',
     '<p>Digital marketing đóng vai trò quan trọng trong việc xây dựng thương hiệu và tăng trưởng doanh thu. Bài viết này giải thích lý do tại sao digital marketing là công cụ không thể thiếu trong chiến lược phát triển thương hiệu hiện đại.</p>',
     'admin', 2, NOW()),

    ('Khám phá các công cụ phân tích dữ liệu mạnh mẽ nhất hiện nay', 'kham-pha-cac-cong-cu-phan-tich-du-lieu-manh-me-nhat-hien-nay',
     '<p>Công nghệ phân tích dữ liệu đang phát triển mạnh mẽ. Bài viết này giới thiệu một số công cụ phân tích dữ liệu mạnh mẽ và cách sử dụng chúng để tối ưu hóa quyết định kinh doanh của bạn.</p>',
     'admin', 2, NOW()),

    ('5 cách để tối ưu hóa chiến lược tiếp thị của bạn', '5-cach-de-toi-uu-hoa-chien-luoc-tiep-thi-cua-ban',
     '<p>Tiếp thị là một yếu tố quan trọng trong việc xây dựng thương hiệu và thu hút khách hàng. Bài viết này sẽ cung cấp các cách thức giúp bạn tối ưu hóa chiến lược tiếp thị của mình để đạt được kết quả tốt nhất.</p>',
     'admin', 2, NOW());



INSERT INTO tags (name) VALUES
                            ('Công nghệ'),
                            ('Blockchain'),
                            ('AI'),
                            ('Y tế'),
                            ('Kinh doanh'),
                            ('Tiếp thị'),
                            ('Năng suất'),
                            ('Du lịch'),
                            ('Sức khỏe'),
                            ('Mạng xã hội'),
                            ('Thương mại điện tử'),
                            ('5G'),
                            ('Tiền điện tử'),
                            ('Làm việc từ xa'),
                            ('Phương tiện điện tử');

INSERT INTO blog_tag (blog_id, tag_id) VALUES
                                           (1, 1), (1, 3), (1, 12), -- Blog 1: Công nghệ, AI, 5G
                                           (2, 7), -- Blog 2: Năng suất
                                           (3, 8), -- Blog 3: Du lịch
                                           (4, 3), (4, 4), -- Blog 4: AI, Y tế
                                           (5, 2), -- Blog 5: Blockchain
                                           (6, 6), -- Blog 6: Tiếp thị
                                           (7, 7), -- Blog 7: Năng suất
                                           (8, 10), (8, 6), -- Blog 8: Mạng xã hội, Tiếp thị
                                           (9, 9), -- Blog 9: Sức khỏe
                                           (10, 14), -- Blog 10: Làm việc từ xa
                                           (11, 10), -- Blog 11: Mạng xã hội
                                           (12, 15), -- Blog 12: Phương tiện điện tử
                                           (13, 6), -- Blog 13: Tiếp thị
                                           (14, 14), -- Blog 14: Làm việc từ xa
                                           (15, 5), -- Blog 15: Kinh doanh
                                           (16, 12), -- Blog 16: 5G
                                           (17, 3), (17, 5), -- Blog 17: AI, Kinh doanh
                                           (18, 13), -- Blog 18: Tiền điện tử
                                           (19, 11); -- Blog 19: Thương mại điện tử



INSERT INTO blog_statistic (id, likes, views, create_at, updated_at) VALUES
                                                                         (1, 0, 0, NOW(), NOW()),
                                                                         (2, 0, 0, NOW(), NOW()),
                                                                         (3, 0, 0, NOW(), NOW()),
                                                                         (4, 0, 0, NOW(), NOW()),
                                                                         (5, 0, 0, NOW(), NOW()),
                                                                         (6, 0, 0, NOW(), NOW()),
                                                                         (7, 0, 0, NOW(), NOW()),
                                                                         (8, 0, 0, NOW(), NOW()),
                                                                         (9, 0, 0, NOW(), NOW()),
                                                                         (10, 0, 0, NOW(), NOW()),
                                                                         (11, 0, 0, NOW(), NOW()),
                                                                         (12, 0, 0, NOW(), NOW()),
                                                                         (13, 0, 0, NOW(), NOW()),
                                                                         (14, 0, 0, NOW(), NOW()),
                                                                         (15, 0, 0, NOW(), NOW()),
                                                                         (16, 0, 0, NOW(), NOW()),
                                                                         (17, 0, 0, NOW(), NOW()),
                                                                         (18, 0, 0, NOW(), NOW()),
                                                                         (19, 0, 0, NOW(), NOW()),
                                                                         (20, 0, 0, NOW(), NOW()),
                                                                         (21, 0, 0, NOW(), NOW()),
                                                                         (22, 0, 0, NOW(), NOW()),
                                                                         (23, 0, 0, NOW(), NOW()),
                                                                         (24, 0, 0, NOW(), NOW()),
                                                                         (25, 0, 0, NOW(), NOW());

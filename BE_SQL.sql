CREATE DATABASE SWP
USE SWP
SELECT * FROM Blog
SELECT * FROM BloodComponents
SELECT * FROM BloodInventory
SELECT * FROM BloodRequests
SELECT * FROM BloodTypes
SELECT * FROM ChatLogs
SELECT * FROM DonationRegistrations
SELECT * FROM Notifications
SELECT * FROM Reports
SELECT * FROM Roles
SELECT * FROM UrgentRequest
SELECT * FROM UserDetails
SELECT * FROM UserProfile
SELECT * FROM Users
SELECT * FROM VnPayPayments
SELECT * FROM Transfusions
SELECT * FROM Donations
SELECT * FROM BloodUnits

-- Vai trò
INSERT INTO Roles (Name) VALUES (N'Thành viên'), (N'Nhân viên'), (N'Quản trị viên');

-- Người dùng
INSERT INTO Users (UserName, Password, IsEnable, Role_Id, Email) VALUES 
('john_doe', 'hashedpassword123', 1, 1, 'john@example.com'),
('jane_staff', 'hashedpassword456', 1, 2, 'jane@example.com');

-- Chi tiết người dùng
INSERT INTO UserDetails (FirstName, LastName, Phone, Address, DOB, Gender, User_Id) VALUES 
('John', 'Doe', '0123456789', N'Hà Nội', '1990-05-10', N'Nam', 1),
('Jane', 'Staff', '0987654321', N'HCM', '1985-09-20', N'Nữ', 2);

-- Hồ sơ người dùng
INSERT INTO UserProfile (User_Id, full_name, dob, gender, blood_type, address, phone, last_donation_date, recovery_time, location, latitude, longitude) VALUES 
(1, 'John Doe', '1990-05-10', N'Nam', 'A+', N'Hà Nội', '0123456789', '2025-05-01', 90, N'Hà Nội', 21.0285, 105.8542),
(2, 'Jane Staff', '1985-09-20', N'Nữ', 'O-', N'HCM', '0987654321', NULL, 0, N'HCM', 10.7626, 106.6602);

-- Nhóm máu
INSERT INTO BloodTypes (Description) VALUES 
('A+'), ('A-'), ('B+'), ('B-'), ('AB+'), ('AB-'), ('O+'), ('O-');

-- Thành phần máu
INSERT INTO BloodComponents (NameBloodComponent) VALUES 
(N'Hồng cầu'), (N'Huyết tương'), (N'Tiểu cầu');

-- Quy tắc tương thích
INSERT INTO CompatibilityRules (donor_type, recipient_type, component_id, is_compatible) VALUES 
(1, 1, 1, 1),
(8, 1, 1, 1),
(2, 2, 2, 1),
(4, 4, 3, 1);

---------------------------------------------------------------------------------------------

-- Đăng ký hiến máu
INSERT INTO DonationRegistrations (User_Id, ready_date, status)
VALUES (1, '2025-06-01', N'Xác nhận');

-- Đơn hiến máu (liên kết đúng registration_id = 1)
INSERT INTO Donations (User_Id, registration_id, blood_type, component_id, donation_date, volume_ml, location, notes)
VALUES (1, 1, 1, 1, '2025-06-01', 450, N'Hà Nội', N'Người hiến khoẻ mạnh, thường xuyên.');

-- Tạo đơn vị máu từ lần hiến (giả sử Donation_Id = 1)
INSERT INTO BloodUnits (BloodType, component_id, donation_id, quantity_ml, expiration_date, status, stored_at)
VALUES (1, 1, 1, 300, '2025-07-01', N'Đã lưu trữ', '2025-06-01 09:30:00');

---------------------------------------------------------------------------------------------

-- Yêu cầu máu
INSERT INTO BloodRequests (RequesterID, BloodType, ComponentID, QuantityML, UrgencyLevel, Status, CreatedAt)
VALUES (1, 1, 1, 200, N'Trung bình', N'Đang xử lý', '2025-06-01 08:00:00');

-- Truyền máu (giả sử request_id = 1, unit_id = 1)
INSERT INTO Transfusions (recipient_id, request_id, blood_unit_id, transfusion_date, status, notes)
VALUES (1, 1, 1, '2025-06-02 10:15:00', N'Hoàn thành', N'Không có biến chứng.');

---------------------------------------------------------------------------------------------

-- 3.5. ➕ Cập nhật kho máu
INSERT INTO BloodInventory (BloodType, ComponentID, TotalQuantityML, LastUpdated)
VALUES (1, 1, 300, GETDATE());

---------------------------------------------------------------------------------------------

-- Thông báo
INSERT INTO Notifications (user_id, content, sent_at, [read])
VALUES (1, N'Yêu cầu máu của bạn đang được xử lý.', '2025-06-01 08:30:00', 0);

-- Yêu cầu khẩn cấp
INSERT INTO UrgentRequest (HospitalName, BloodType, Units, RequestDate, Status, User_Id)
VALUES (N'Phòng khám Chữ thập đỏ', 'A-', 2, '2025-06-06', N'Đã duyệt', 2);

-- Thanh toán VnPay
INSERT INTO VnPayPayments (User_Id, Amount, Payment_Time, Transaction_Code, Status)
VALUES (2, 750.00, '2025-06-04 16:30:00', 'VNP987654321', N'Đang chờ');

-- Nhật ký trò chuyện
INSERT INTO ChatLogs (User_Id, message, sender, created_at)
VALUES (2, N'Tôi cần hỗ trợ đăng ký.', N'Nhân viên', '2025-06-02 09:15:00');

-- Báo cáo hệ thống
INSERT INTO Reports (report_type, generated_by, created_at, content)
VALUES (N'Cảnh báo trong ngày', 2, '2025-06-02 08:00:00', N'Hệ thống phát hiện lượng máu O- thấp.');

---------------------------------------------------------------------------------------------


INSERT INTO Blog (Title, User_Id, Content, Created_at, Status) VALUES 
(N'Vì sao nên hiến máu', 1, N'Hiến máu là một hành động nhân đạo cứu người.', '2025-06-01 09:00:00', N'Đã đăng'),
(N'Cơ bản về tương thích máu', 2, N'Hiểu rõ các nhóm máu và tương thích là rất quan trọng.', '2025-06-01 10:00:00', N'Đã đăng');


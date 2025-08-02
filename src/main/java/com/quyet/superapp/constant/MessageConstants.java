package com.quyet.superapp.constant;

public class MessageConstants {
    public static final String REGISTER_SUCCESS = "Đăng ký thành công";
    public static final String REGISTER_FAILED = "Đăng ký thất bại. Vui lòng thử lại sau.";
    public static final String USERNAME_EXISTS = "Tên đăng nhập đã tồn tại";
    public static final String EMAIL_EXISTS = "Email đã tồn tại";
    public static final String CCCD_EXISTS = "CCCD đã tồn tại trong hệ thống";
    public static final String EMAIL_PROFILE_EXISTS = "Email đã tồn tại trong hồ sơ người dùng";
    public static final String ROLE_NOT_FOUND = "Không tìm thấy vai trò";
    public static final String WARD_NOT_FOUND = "Không tìm thấy phường/xã phù hợp";

    public static final String LOGIN_FAILED = "Tài khoản hoặc mật khẩu không đúng";
    public static final String USER_NOT_FOUND = "Tài khoản không tồn tại";

    // Đăng ký hiến máu

    public static final String DONATION_ALREADY_EXISTS = "Đã tồn tại bản ghi hiến máu cho đơn này";
    public static final String DONATION_REGISTRATION_NOT_FOUND = "Không tìm thấy đơn đăng ký hiến máu";
    public static final String BLOOD_TYPE_NOT_FOUND = "Không tìm thấy nhóm máu phù hợp";
    public static final String ADDRESS_NOT_FOUND = "Không tìm thấy địa chỉ phù hợp";
    public static final String HEALTH_NOT_ELIGIBLE = "Người hiến máu không đạt yêu cầu sức khỏe";
    public static final String INVALID_REGISTRATION_STATUS = "Trạng thái đơn đăng ký không hợp lệ";

    public static final String DUPLICATE_PENDING = "Bạn đã có đơn đăng ký đang chờ.";
    public static final String MISSING_PROFILE = "Bạn cần cập nhật hồ sơ trước khi đăng ký hiến máu.";
    public static final String SLOT_FULL = "⛔ Slot đã đủ 10 người, vui lòng chọn khung giờ khác.";

    public static final String BLOOD_TYPE_NOT_FOUND_WITH_DESC = "Không tìm thấy nhóm máu: ";
    public static final String INVALID_CONFIRM_STATUS = "Đơn đăng ký không ở trạng thái chờ xác nhận.";
    public static final String DONATION_CONFIRMED_EMAIL_SUBJECT = "Xác nhận hiến máu thành công";
    public static final String ONLY_CONFIRMED_CAN_BE_DONATED = "Chỉ những đơn đã xác nhận mới được đánh dấu là đã hiến.";
    public static final String CANNOT_CANCEL_CURRENT_STATE = "Không thể hủy đơn ở trạng thái hiện tại.";

}

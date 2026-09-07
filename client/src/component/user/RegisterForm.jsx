import { useState } from "react";
import { CalendarDays, ChevronDown } from "lucide-react";

const RegisterForm = () => {
  const [formData, setFormData] = useState({
    firstName: "",
    lastName: "",
    phoneNumber: "",
    email: "",
    password: "",
    confirmPassword: "",
    dateOfBirth: "",
    gender: "",
    province: "",
    agreePolicy: false,
  });

  const handleChange = (e) => {
    const { name, value, type, checked } = e.target;

    setFormData((prev) => ({
      ...prev,
      [name]: type === "checkbox" ? checked : value,
    }));
  };

  const handleSubmit = (e) => {
    e.preventDefault();

    if (!formData.agreePolicy) {
      alert("Vui lòng đồng ý với chính sách và điều khoản.");
      return;
    }

    if (formData.password !== formData.confirmPassword) {
      alert("Mật khẩu xác nhận không khớp.");
      return;
    }

    console.log("Register data:", formData);

    // TODO:
    // Gọi API đăng ký tại đây
  };

  return (
    <div className="min-h-screen bg-white px-4 py-6 sm:px-6">
      <div className="mx-auto w-full max-w-[530px]">
        {/* Title */}
        <h1 className="mb-8 text-center text-[26px] font-extrabold uppercase tracking-wide text-[#ed1b2f]">
          Đăng ký tài khoản
        </h1>

        <form onSubmit={handleSubmit} className="space-y-4">
          {/* Họ */}
          <div>
            <input
              type="text"
              name="lastName"
              value={formData.lastName}
              onChange={handleChange}
              placeholder="Họ *"
              required
              className="h-[43px] w-full rounded-[6px] border border-[#a8a8a8] px-2 text-[12px] text-gray-700 outline-none placeholder:text-gray-600 focus:border-[#ed1b2f] focus:ring-1 focus:ring-[#ed1b2f]"
            />
          </div>

          {/* Tên */}
          <div>
            <input
              type="text"
              name="firstName"
              value={formData.firstName}
              onChange={handleChange}
              placeholder="Tên *"
              required
              className="h-[43px] w-full rounded-[6px] border border-[#a8a8a8] px-2 text-[12px] text-gray-700 outline-none placeholder:text-gray-600 focus:border-[#ed1b2f] focus:ring-1 focus:ring-[#ed1b2f]"
            />
          </div>

          {/* Số điện thoại */}
          <div>
            <input
              type="tel"
              name="phoneNumber"
              value={formData.phoneNumber}
              onChange={handleChange}
              placeholder="Số điện thoại *"
              required
              className="h-[43px] w-full rounded-[6px] border border-[#a8a8a8] px-2 text-[12px] text-gray-700 outline-none placeholder:text-gray-600 focus:border-[#ed1b2f] focus:ring-1 focus:ring-[#ed1b2f]"
            />
          </div>

          {/* Email */}
          <div>
            <input
              type="email"
              name="email"
              value={formData.email}
              onChange={handleChange}
              placeholder="E-mail"
              className="h-[43px] w-full rounded-[6px] border border-[#a8a8a8] px-2 text-[12px] text-gray-700 outline-none placeholder:text-gray-600 focus:border-[#ed1b2f] focus:ring-1 focus:ring-[#ed1b2f]"
            />
          </div>

          {/* Mật khẩu */}
          <div>
            <input
              type="password"
              name="password"
              value={formData.password}
              onChange={handleChange}
              placeholder="Mật khẩu *"
              required
              className="h-[43px] w-full rounded-[6px] border border-[#a8a8a8] px-2 text-[12px] text-gray-700 outline-none placeholder:text-gray-600 focus:border-[#ed1b2f] focus:ring-1 focus:ring-[#ed1b2f]"
            />
          </div>

          {/* Xác nhận mật khẩu */}
          <div>
            <input
              type="password"
              name="confirmPassword"
              value={formData.confirmPassword}
              onChange={handleChange}
              placeholder="Xác nhận mật khẩu *"
              required
              className="h-[43px] w-full rounded-[6px] border border-[#a8a8a8] px-2 text-[12px] text-gray-700 outline-none placeholder:text-gray-600 focus:border-[#ed1b2f] focus:ring-1 focus:ring-[#ed1b2f]"
            />
          </div>

          {/* Ngày sinh */}
          <div className="relative">
            <input
              type="date"
              name="dateOfBirth"
              value={formData.dateOfBirth}
              onChange={handleChange}
              required
              className="date-input h-[43px] w-full appearance-none rounded-[6px] border border-[#a8a8a8] bg-white px-2 pr-10 text-[12px] text-gray-700 outline-none focus:border-[#ed1b2f] focus:ring-1 focus:ring-[#ed1b2f]"
            />

            <CalendarDays
              size={17}
              strokeWidth={1.5}
              className="pointer-events-none absolute right-2 top-1/2 -translate-y-1/2 text-gray-600"
            />
          </div>

          {/* Giới tính */}
          <div className="relative">
            <select
              name="gender"
              value={formData.gender}
              onChange={handleChange}
              required
              className="h-[43px] w-full appearance-none rounded-[6px] border border-[#a8a8a8] bg-white px-2 pr-10 text-[12px] text-gray-700 outline-none focus:border-[#ed1b2f] focus:ring-1 focus:ring-[#ed1b2f]"
            >
              <option value="" disabled>
                Chọn giới tính *
              </option>
              <option value="male">Nam</option>
              <option value="female">Nữ</option>
              <option value="other">Khác</option>
            </select>

            <ChevronDown
              size={17}
              strokeWidth={1.5}
              className="pointer-events-none absolute right-1.5 top-1/2 -translate-y-1/2 text-gray-700"
            />
          </div>

          {/* Tỉnh thành */}
          <div className="relative">
            <select
              name="province"
              value={formData.province}
              onChange={handleChange}
              required
              className="h-[43px] w-full appearance-none rounded-[6px] border border-[#a8a8a8] bg-white px-2 pr-10 text-[12px] text-gray-700 outline-none focus:border-[#ed1b2f] focus:ring-1 focus:ring-[#ed1b2f]"
            >
              <option value="" disabled>
                Chọn tỉnh thành *
              </option>
              <option value="hanoi">Hà Nội</option>
              <option value="hochiminh">TP. Hồ Chí Minh</option>
              <option value="danang">Đà Nẵng</option>
              <option value="haiphong">Hải Phòng</option>
              <option value="cantho">Cần Thơ</option>
            </select>

            <ChevronDown
              size={17}
              strokeWidth={1.5}
              className="pointer-events-none absolute right-1.5 top-1/2 -translate-y-1/2 text-gray-700"
            />
          </div>

          {/* Checkbox - Chính sách */}
          <div className="flex items-start gap-2 pt-1">
            <input
              type="checkbox"
              id="agreePolicy"
              name="agreePolicy"
              checked={formData.agreePolicy}
              onChange={handleChange}
              className="mt-[1px] h-[17px] w-[17px] shrink-0 cursor-pointer appearance-none rounded-[2px] border border-gray-400 checked:border-[#ed1b2f] checked:bg-[#ed1b2f] checked:after:block checked:after:ml-[4px] checked:after:mt-[1px] checked:after:h-[9px] checked:after:w-[5px] checked:after:rotate-45 checked:after:border-b-2 checked:after:border-r-2 checked:after:border-white"
            />

            <label
              htmlFor="agreePolicy"
              className="cursor-pointer text-[11px] leading-[17px] text-gray-700"
            >
              Đồng ý với{" "}
              <a
                href="/terms"
                className="text-blue-600 underline hover:text-blue-800"
              >
                Chính sách, quy định chung và Thông báo bảo mật cá nhân
              </a>
            </label>
          </div>

          {/* Bottom */}
          <div className="flex flex-col items-center gap-4 pt-2 sm:flex-row sm:justify-between">
            {/* Register button */}
            <button
              type="submit"
              className="h-[42px] w-full rounded-[6px] bg-[#ed1b2f] px-10 text-[12px] font-bold uppercase text-white transition hover:bg-[#d91529] active:scale-[0.98] sm:w-[160px]"
            >
              Đăng ký
            </button>

            {/* Login */}
            <p className="text-[11px] text-gray-700">
              Bạn đã có tài khoản?{" "}
              <a
                href="/login"
                className="text-blue-600 underline hover:text-blue-800"
              >
                Đăng nhập
              </a>
            </p>
          </div>
        </form>
      </div>
    </div>
  );
};

export default RegisterForm;

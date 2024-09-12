import { EyeFilledIcon } from "@/assets/EyeFilledIcon";
import { EyeSlashFilledIcon } from "@/assets/EyeSlashFilledIcon";
import { APIURL, http } from "@/assets/http";
import { MailIcon } from "@/assets/MailIcon";
import { Button, Checkbox, Input } from "@nextui-org/react";
import { useState } from "react";

interface Message {
  color: string;
  message: string;
}

export default function Register() {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [isVisible, setIsVisible] = useState(false);
  const [isLoading, setLoading] = useState(false);
  const [message, setMessage] = useState<Message | null>(null);
  const [isChecked, setIsChecked] = useState(false);

  const toggleVisibility = () => setIsVisible(!isVisible);

  const handleEmailChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setEmail(e.target.value);
  };

  const handlePasswordChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setPassword(e.target.value);
  };

  const handleCheckboxChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setIsChecked(e.target.checked);
  };

  const handleRegisterButton = async () => {
    if (!isChecked) {
      setMessage({
        message: "You must accept the terms and conditions.",
        color: "red",
      });
      return;
    }

    try {
      setLoading(true);
      const response = await http.post(`${APIURL}/accounts/register`, {
        email,
        password,
      });

      const loginResponse = await http.post(`${APIURL}/accounts/login`, {
        email,
        password,
      });

      const jwt = loginResponse.data.accessToken;
      const userId = response.data.id;
      localStorage.setItem("jwtToken", jwt);
      localStorage.setItem("userId", userId);

      setMessage({ message: "Registration successful!", color: "green" });

      setTimeout(() => {
        window.location.href = "/";
      }, 1000);
    } catch (error: any) {
      console.log(error);
      setMessage({
        message: `Registration failed. ${error.response.data.message}`,
        color: "red",
      });
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="bg-gradient-to-r from-blue-200 to-pink-200">
      <div className="flex items-center justify-center flex-col h-screen">
        <div className="bg-slate-200 p-4 rounded-3xl">
          <div className="font-semibold text-2xl my-1 grid place-items-center">
            Sign Up 👋
          </div>

          <div id="email-ve-password-yeri">
            <div className="my-3 w-96" id="email">
              <Input
                endContent={
                  <MailIcon className="text-2xl text-default-400 pointer-events-none flex-shrink-0" />
                }
                className=""
                value={email}
                onChange={handleEmailChange}
                type="email"
                label="Email"
                placeholder="Enter your email"
              />
            </div>
            <div className="my-3 w-96" id="password">
              <Input
                label="Password"
                placeholder="Enter your password"
                endContent={
                  <button
                    type="button"
                    onClick={toggleVisibility}
                    aria-label="toggle password visibility"
                  >
                    {isVisible ? (
                      <EyeSlashFilledIcon className="text-2xl text-default-400 pointer-events-none" />
                    ) : (
                      <EyeFilledIcon className="text-2xl text-default-400 pointer-events-none" />
                    )}
                  </button>
                }
                type={isVisible ? "text" : "password"}
                value={password}
                onChange={handlePasswordChange}
              />
            </div>
          </div>
          <div className="my-1">
            <Checkbox checked={isChecked} onChange={handleCheckboxChange}>
              I accept the terms of registration
            </Checkbox>
          </div>
          <div id="registerbutonu" className="grid place-items-center">
            <Button
              isLoading={isLoading}
              className="w-96"
              onPress={handleRegisterButton}
              color="success"
            >
              Register!
            </Button>
          </div>
        </div>
        <div
          className="p-4 rounded-3xl my-3 text-white text-sm font-poppins"
          style={{ backgroundColor: message?.color || "transparent" }}
          id="alttaki-response-yeri"
        >
          {message?.message}
        </div>
      </div>
    </div>
  );
}

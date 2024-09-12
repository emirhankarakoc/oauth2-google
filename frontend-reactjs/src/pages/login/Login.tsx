import { Button, Input } from "@nextui-org/react";
import { useEffect, useState } from "react";
import { GoogleLogin, googleLogout, useGoogleLogin } from "@react-oauth/google";
import { jwtDecode } from "jwt-decode";
import { APIURL, http, httpError } from "@/assets/http"; // APIURL ve http importlarınızı kontrol edin
import { EyeSlashFilledIcon } from "@/assets/EyeSlashFilledIcon";
import { EyeFilledIcon } from "@/assets/EyeFilledIcon";
import { MailIcon } from "@/assets/MailIcon";
import axios from "axios";

interface Message {
  color: string;
  message: string;
}

export default function Login() {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [isVisible, setIsVisible] = useState(false);
  const [isLoading, setLoading] = useState(false);
  const [message, setMessage] = useState<Message | null>(null);

  const toggleVisibility = () => setIsVisible(!isVisible);

  const handleEmailChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setEmail(e.target.value);
  };

  const handlePasswordChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setPassword(e.target.value);
  };

  const handleLoginButton = async () => {
    try {
      setLoading(true);
      const response = await http.post(`${APIURL}/accounts/login`, {
        email,
        password,
      });

      const jwt = response.data.accessToken;
      const userId = response.data.id;
      localStorage.setItem("jwtToken", jwt);
      localStorage.setItem("userId", userId);

      setMessage({ message: "Login successful!", color: "green" });

      setTimeout(() => {
        window.location.href = "/";
      }, 1000);
    } catch (error: any) {
      console.log(error);
      setMessage({
        message: `Login failed. ${error.response.data.message}`,
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
            Log In 👋
          </div>

          <div id="email-ve-password-yeri">
            <div className="my-3 w-96" id="email">
              <Input
                endContent={
                  <MailIcon className="text-2xl text-default-400 pointer-events-none flex-shrink-0" />
                }
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
          <div id="loginbutonu" className="grid place-items-center">
            <Button
              isLoading={isLoading}
              className="w-96"
              onPress={handleLoginButton}
              color="success"
            >
              Log In!
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

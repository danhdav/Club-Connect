import React, { useState, ChangeEvent, FormEvent } from "react";
import { useNavigate, Link } from "react-router-dom";
import axios from "axios";

import { Button } from "../../components/ui/button";
import { Card, CardContent } from "../../components/ui/card";
import { Input } from "../../components/ui/input";

export const Login = (): JSX.Element => {
  const [email, setEmail]                 = useState<string>("");
  const [password, setPassword]           = useState<string>("");
  const [responseMessage, setResponseMessage] = useState<string>("");
  const [isSubmitting, setIsSubmitting]   = useState<boolean>(false);
  const navigate = useNavigate();

  const handleChange =
    (setter: React.Dispatch<React.SetStateAction<string>>) =>
    (e: ChangeEvent<HTMLInputElement>) => {
      setter(e.target.value);
      setResponseMessage("");
    };

  const validateForm = (): boolean => {
    setResponseMessage("");
    if (!email.trim() || !password) {
      setResponseMessage("Please enter both email and password.");
      return false;
    }
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (!emailRegex.test(email)) {
      setResponseMessage("Please enter a valid email address.");
      return false;
    }
    return true;
  };

  const handleLogin = async (e?: FormEvent) => {
    if (e) e.preventDefault();
    if (!validateForm()) return;
    setIsSubmitting(true);

    let response;
    try {
      // strip out any newline chars
      const sanitizedEmail = email.replace(/[\r\n]/g, "");
      const sanitizedPassword = password.replace(/[\r\n]/g, "");
      response = await axios.post("http://localhost:8080/auth/login", {
        email: sanitizedEmail,
        password: sanitizedPassword,
      });
    } catch (err) {
      console.error("Network or server error:", err);
      if (axios.isAxiosError(err) && err.response) {
        setResponseMessage(
          err.response.data.message || "Login failed – check credentials."
        );
      } else {
        setResponseMessage("Login failed. Please try again later.");
      }
      setIsSubmitting(false);
      return;
    }

    // POST succeeded; now extract whatever your API sent back
    const data = response.data as any;
    const id   = data.id ?? data.userId;
    if (id != null) {
      localStorage.setItem("userId", id.toString());
    } else if (data.token) {
      localStorage.setItem("token", data.token);
    } else {
      console.warn("Login response missing id/token", data);
    }

    setIsSubmitting(false);
    navigate("/home");
  };

  return (
    <div className="flex flex-col w-full min-h-screen bg-[#e87500]">
      <div className="flex flex-1 relative overflow-hidden">
        {/* Green gradient background */}
        <div className="absolute top-0 left-0 w-3/4 h-full [background:linear-gradient(90deg,rgba(113,156,122,1)_0%,rgba(21,71,52,1)_100%)]" />

        <div className="flex flex-col w-full items-center justify-center relative z-10">
          <div className="w-full max-w-[980px] mx-auto mt-16 mb-16">
            <Card className="w-full h-[727px] bg-white border-[10px] border-[#154734] rounded-none shadow-none">
              <CardContent className="p-0 h-full relative">
                <form onSubmit={handleLogin} className="h-full">
                  <h1 className="absolute top-3 left-[18px] text-8xl font-[Permanent_Marker] text-black [text-shadow:8px_8px_4px_#00000040]">
                    Club&nbsp;Connect
                  </h1>

                  {/* Email */}
                  <label className="absolute top-[183px] left-[18px] text-5xl font-[Source_Serif_Pro] text-black">
                    Login:
                  </label>
                  <div className="absolute top-[260px] left-[73px] w-[776px] h-[102px] flex items-center">
                    <div className="h-full w-[10px] bg-[#e87500]" />
                    <Input
                      type="email"
                      placeholder="you@example.com"
                      value={email}
                      onChange={handleChange(setEmail)}
                      className="h-full w-full border-none rounded-none [background:linear-gradient(90deg,rgba(255,246,238,1)_0%,rgba(255,225,195,1)_100%)] focus-visible:ring-0"
                      required
                    />
                  </div>

                  {/* Password */}
                  <label className="absolute top-[392px] left-[18px] text-5xl font-[Source_Serif_Pro] text-black">
                    Password:
                  </label>
                  <div className="absolute top-[472px] left-[73px] w-[776px] h-[102px] flex items-center">
                    <div className="h-full w-[10px] bg-[#e87500]" />
                    <Input
                      type="password"
                      placeholder="••••••••"
                      value={password}
                      onChange={handleChange(setPassword)}
                      className="h-full w-full border-none rounded-none [background:linear-gradient(90deg,rgba(255,246,238,1)_0%,rgba(255,225,195,1)_100%)] focus-visible:ring-0"
                      required
                    />
                  </div>
                </form>
              </CardContent>
            </Card>

            <div className="flex justify-between items-center mt-8 px-4">
              <Link
                to="/signup"
                className="text-[32px] font-[Source_Serif_Pro] text-[#1eb4cf] hover:underline"
              >
                Sign Up
              </Link>
              <Button
                onClick={(e) => handleLogin(e)}
                disabled={isSubmitting}
                className="w-[196px] h-[86px] bg-[#e87500] hover:bg-[#e87500]/90 border-[5px] border-[#154734] rounded-none font-[Source_Serif_Pro] text-5xl"
              >
                {isSubmitting ? "..." : "Login"}
              </Button>
            </div>
          </div>

          {responseMessage && (
            <div className="w-full text-center absolute bottom-10">
              <p className="text-xl text-red-600">{responseMessage}</p>
            </div>
          )}
        </div>
      </div>
    </div>
  );
};

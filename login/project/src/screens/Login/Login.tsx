import React from "react";
import { Button } from "../../components/ui/button";
import { Card, CardContent } from "../../components/ui/card";
import { Input } from "../../components/ui/input";

export const Login = (): JSX.Element => {
  return (
    <div className="bg-[#e87500] flex flex-row justify-center w-full min-h-screen">
      <div className="bg-[#e87500] w-full max-w-[1728px] h-full relative">
        <div className="relative w-full max-w-[1566px] h-full min-h-[1117px]">
          <div className="absolute w-full max-w-[1497px] h-full min-h-[1117px] top-0 left-0">
            {/* Green gradient background */}
            <div className="absolute w-full max-w-[1328px] h-full min-h-[1117px] top-0 left-0 [background:linear-gradient(90deg,rgba(113,156,122,1)_0%,rgba(21,71,52,1)_100%)]" />

            {/* Login card container */}
            <div className="absolute w-full max-w-[831px] top-[339px] left-[666px]">
              <Card className="relative w-[980px] h-[727px] -left-7 -top-[23px] bg-white border-[10px] border-solid border-[#154734] rounded-none shadow-none">
                <CardContent className="p-0">
                  {/* Title */}
                  <h1 className="absolute top-3 left-[18px] [text-shadow:8px_8px_4px_#00000040] [font-family:'Permanent_Marker',Helvetica] font-normal text-black text-8xl tracking-[0] leading-[normal]">
                    Club Connect
                  </h1>

                  {/* Login label */}
                  <label className="absolute top-[183px] left-[18px] [font-family:'Source_Serif_Pro',Helvetica] font-normal text-black text-5xl tracking-[0] leading-[normal]">
                    Login:
                  </label>

                  {/* Login input */}
                  <div className="absolute w-[776px] h-[102px] top-[260px] left-[73px] flex items-center">
                    <div className="h-full w-[10px] bg-[#e87500]"></div>
                    <Input className="h-full w-full border-none rounded-none [background:linear-gradient(90deg,rgba(255,246,238,1)_0%,rgba(255,225,195,1)_100%)] focus-visible:ring-0 focus-visible:ring-offset-0" />
                  </div>

                  {/* Password label */}
                  <label className="absolute top-[392px] left-[18px] [font-family:'Source_Serif_Pro',Helvetica] font-normal text-black text-5xl tracking-[0] leading-[normal]">
                    Password:
                  </label>

                  {/* Password input */}
                  <div className="absolute w-[776px] h-[102px] top-[472px] left-[73px] flex items-center">
                    <div className="h-full w-[10px] bg-[#e87500]"></div>
                    <Input
                      type="password"
                      className="h-full w-full border-none rounded-none [background:linear-gradient(90deg,rgba(255,246,238,1)_0%,rgba(255,225,195,1)_100%)] focus-visible:ring-0 focus-visible:ring-offset-0"
                    />
                  </div>
                </CardContent>
              </Card>
            </div>

            {/* Sign up link */}
            <a
              href="#"
              className="absolute top-[927px] left-[694px] [font-family:'Source_Serif_Pro',Helvetica] font-normal text-[#1eb4cf] text-[32px] tracking-[0] leading-[normal] hover:underline"
            >
              Sign Up
            </a>
          </div>

          {/* Login button */}
          <Button className="absolute w-[196px] h-[86px] top-[928px] left-[1370px] bg-[#e87500] hover:bg-[#e87500]/90 border-[5px] border-solid border-[#154734] rounded-none [font-family:'Source_Serif_Pro',Helvetica] font-normal text-5xl tracking-[0] leading-[normal] p-0">
            Login
          </Button>
        </div>
      </div>
    </div>
  );
};

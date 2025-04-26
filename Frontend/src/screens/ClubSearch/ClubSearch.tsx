import React from "react";
import { Card, CardContent } from "../../components/ui/card";
import { Input } from "../../components/ui/input";

export const ClubSearch = (): JSX.Element => {
  // Club data for mapping
  const clubs = [
    {
      id: 1,
      name: "Club Name",
      description: "This is a description of the club",
      tags: ["#Tag1", "#Tag2", "#Tag3"],
    },
    {
      id: 2,
      name: "Club Name",
      description: "This is a description of the club",
      tags: ["#Tag1", "#Tag2", "#Tag3"],
    },
    {
      id: 3,
      name: "Club Name",
      description: "This is a description of the club",
      tags: ["#Tag1", "#Tag2", "#Tag3"],
    },
    {
      id: 4,
      name: "Club Name",
      description: "This is a description of the club",
      tags: ["#Tag1", "#Tag2", "#Tag3"],
    },
    {
      id: 5,
      name: "Club Name",
      description: "This is a description of the club",
      tags: ["#Tag1", "#Tag2", "#Tag3"],
    },
    {
      id: 6,
      name: "Club Name",
      description: "This is a description of the club",
      tags: ["#Tag1", "#Tag2", "#Tag3"],
    },
    {
      id: 7,
      name: "Club Name",
      description: "This is a description of the club",
      tags: ["#Tag1", "#Tag2", "#Tag3"],
    },
  ];

  return (
    <div className="bg-white flex flex-row justify-center w-full">
      <div className="bg-white relative w-full max-w-[1728px] min-h-screen overflow-hidden">
        {/* Logo */}
        <div className="w-[300px] h-[200px] bg-[url(/logo.svg)] bg-[100%_100%]" />

        {/* Settings */}
        <div className="absolute w-[300px] h-[200px] top-0 right-0 bg-[url(/settings-box.png)] bg-cover bg-[50%_50%]" />

        {/* SearchIcon Bar */}
        <div className="absolute w-[1128px] h-[200px] top-0 left-[300px] bg-[#154734] flex items-center justify-center">
          <div className="relative flex items-center">
            {/* Filter Button */}
            <img
              className="w-[122px] h-[122px] mr-[50px] object-cover"
              alt="Filter button"
              src="/filter-button.png"
            />

            {/* SearchIcon Input */}
            <div className="relative">
              <Input
                className="w-[784px] h-[122px] bg-[#e6e6e6] rounded-none text-2xl px-6"
                placeholder="SearchIcon clubs..."
              />

              {/* SearchIcon Button */}
              <button className="absolute right-0 top-0 h-[122px] w-[122px] bg-transparent flex items-center justify-center">
                <img
                  className="w-full h-full object-cover"
                  alt="SearchIcon button"
                  src="/search-button.svg"
                />
              </button>
            </div>
          </div>
        </div>

        {/* Club List Container */}
        <div className="mt-[200px] p-[50px] border-[10px] border-solid border-[#e87500]">
          {/* Club Cards */}
          <div className="flex flex-col gap-[50px]">
            {clubs.map((club) => (
                <Card
                key={club.id}
                className="w-full h-[250px] bg-[#d9d9d9] rounded-none shadow-none"
                onClick={() => window.location.href = `/home/search/${club.name}`}
                >
                <CardContent className="p-0 h-full relative">
                {/* Club Logo */}
                <div className="absolute w-[200px] h-[200px] top-[25px] left-[50px] bg-[#363636]" />

                {/* Club Info */}
                <div className="ml-[293px] pt-6">
                <h2 className="[font-family:'Source_Serif_Pro',Helvetica] font-bold text-black text-[64px]">
                  {club.name}
                </h2>

                <p className="mt-[24px] [font-family:'Source_Serif_Pro',Helvetica] font-normal text-black text-[32px]">
                  {club.description}
                </p>

                <p className="mt-[24px] [font-family:'Source_Serif_Pro',Helvetica] font-normal text-black text-[32px]">
                  {club.tags.join(" ")}
                </p>
                </div>
                </CardContent>
                </Card>
            ))}
          </div>
        </div>
      </div>
    </div>
  );
};

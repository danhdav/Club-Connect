import React from "react";
import { Badge } from "../../components/ui/badge";
import { Button } from "../../components/ui/button";
import { Card, CardContent } from "../../components/ui/card";

export const MyClubs = (): JSX.Element => {
  // Club data for mapping
  const clubs = [
    {
      id: 1,
      name: "My Club #1",
      description: "This is a description of the club",
      tags: ["Tag1", "Tag2", "Tag3"],
    },
    {
      id: 2,
      name: "My Club #2",
      description: "This is a description of the club",
      tags: ["Tag1", "Tag2", "Tag3"],
    },
    {
      id: 3,
      name: "My Club #3",
      description: "This is a description of the club",
      tags: ["Tag1", "Tag2", "Tag3"],
    },
  ];

  return (
    <main className="bg-white flex flex-col min-h-screen">
      {/* Header */}
      <header className="flex w-full">
        {/* Logo */}
        <div className="w-[300px] h-[200px] bg-[url(/logo.svg)] bg-[100%_100%]" />

        {/* Title */}
        <div className="flex-1 h-[200px] bg-[#154734] flex items-center justify-center">
          <h1 className="font-['Source_Serif_Pro',Helvetica] text-white text-[64px]">
            Manage My Clubs
          </h1>
        </div>

        {/* SettingsIcon */}
        <div className="w-[300px] h-[200px] bg-[url(/settings-box.png)] bg-cover bg-[50%_50%]" />
      </header>

      {/* Main content */}
      <section className="w-full border border-solid border-black bg-white p-6">
        <div className="max-w-[1528px] mx-auto space-y-6 relative">
          {/* Club cards */}
          {clubs.map((club) => (
            <Card key={club.id} className="bg-[#d9d9d9] h-[250px] relative">
              <CardContent className="p-0 h-full flex">
                {/* Club image */}
                <div className="w-[200px] h-[200px] bg-[#363636] m-[25px]" />

                {/* Club info */}
                <div className="flex flex-col pt-6">
                  <h2 className="font-['Source_Serif_Pro',Helvetica] font-bold text-black text-[64px]">
                    {club.name}
                  </h2>
                  <p className="font-['Source_Serif_Pro',Helvetica] text-black text-[32px] mt-2">
                    {club.description}
                  </p>
                  <div className="mt-6 flex gap-2">
                    {club.tags.map((tag) => (
                      <Badge
                        key={tag}
                        variant="outline"
                        className="font-['Source_Serif_Pro',Helvetica] text-black text-[32px] bg-transparent"
                      >
                        #{tag}
                      </Badge>
                    ))}
                  </div>
                </div>

                {/* Edit button */}
                <div className="absolute right-6 top-1/2 -translate-y-1/2">
                  <Button
                    variant="ghost"
                    size="icon"
                    className="w-[154px] h-[154px]"
                  >
                    <img
                      src="/club-edit-2.png"
                      alt="Edit club"
                      className="w-full h-full object-cover"
                    />
                  </Button>
                </div>
              </CardContent>
            </Card>
          ))}
        </div>
      </section>

      {/* Footer */}
      <footer className="w-full h-[180px] bg-[#154734] flex items-center justify-center mt-auto">
        <Button
          variant="ghost"
          className="font-['Source_Serif_Pro',Helvetica] text-white text-[64px] relative"
          onClick={() => {
            window.location.href = "/home/createClub"; // Insert condition to check if login input is valid
          }}
        >
          Create Club
          <div className="absolute -bottom-4 left-1/2 -translate-x-1/2 w-[500px] h-[5px] bg-white" />
        </Button>
      </footer>

      {/* Scroller */}
      <img
        className="fixed right-0 top-0 h-screen w-auto object-cover z-10"
        alt="Scroller"
        src="/scroller.png"
      />
    </main>
  );
};

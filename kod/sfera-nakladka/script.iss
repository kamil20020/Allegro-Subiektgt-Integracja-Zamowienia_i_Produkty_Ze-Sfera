[Setup]
AppName=Moja Aplikacja Subiekt
AppVersion=1.0
DefaultDirName={pf}\Nakladka-Sfera-Subiektgt
DefaultGroupName=Nakladka-Sfera-Subiektgt
OutputBaseFilename=Nakladka-Sfera-Subiektgt

[Files]
Source: "php\*"; DestDir: "{app}\php"; Flags: recursesubdirs
Source: "htdocs\*"; DestDir: "{app}\htdocs"; Flags: recursesubdirs
Source: "start.bat"; DestDir: "{app}"

[Icons]
Name: "{commondesktop}\Uruchom Nakladka Sfera Subiektgt"; Filename: "{app}\start.bat"

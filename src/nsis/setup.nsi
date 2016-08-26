; Installer for howto

;======================================================
; Includes

  !include MUI.nsh
  !include Sections.nsh
  !include ..\..\target\project.nsh
  !include envvarupdate.nsh
  !include ReplaceInFile.nsh

;======================================================
; Installer Information

  Name "${PROJECT_NAME}"
  SetCompressor /SOLID lzma
  XPStyle on
  CRCCheck on
  InstallDir "C:\Program Files\Obdobion\${PROJECT_ARTIFACT_ID}\"
  AutoCloseWindow false
  ShowInstDetails show
  Icon "${NSISDIR}\Contrib\Graphics\Icons\orange-install.ico"

;======================================================
; Version Tab information for Setup.exe properties

  VIProductVersion ${PROJECT_VERSION}.0
  VIAddVersionKey ProductName "${PROJECT_NAME}.0"
  VIAddVersionKey ProductVersion "${PROJECT_VERSION}"
  VIAddVersionKey CompanyName "${PROJECT_ORGANIZATION_NAME}"
  VIAddVersionKey FileVersion "${PROJECT_VERSION}.0"
  VIAddVersionKey FileDescription ""
  VIAddVersionKey LegalCopyright ""

;======================================================
; Variables


;======================================================
; Modern Interface Configuration

  !define MUI_HEADERIMAGE
  !define MUI_ABORTWARNING
  !define MUI_COMPONENTSPAGE_SMALLDESC
  !define MUI_HEADERIMAGE_BITMAP_NOSTRETCH
  !define MUI_FINISHPAGE
  !define MUI_FINISHPAGE_TEXT "Thank you for installing ${PROJECT_NAME}. \r\n\n\nYou can now run ${PROJECT_ARTIFACT_ID} from your command line."
  !define MUI_ICON "${NSISDIR}\Contrib\Graphics\Icons\orange-install.ico"

;======================================================
; Modern Interface Pages

  !define MUI_DIRECTORYPAGE_VERIFYONLEAVE
;  !insertmacro MUI_PAGE_LICENSE howto_license.txt
  !insertmacro MUI_PAGE_DIRECTORY
  !insertmacro MUI_PAGE_COMPONENTS
  !insertmacro MUI_PAGE_INSTFILES
  !insertmacro MUI_PAGE_FINISH

;======================================================
; Languages

  !insertmacro MUI_LANGUAGE "English"

;======================================================
; Installer Sections


Section "howto"

    SetOutPath $INSTDIR
    SetOverwrite on
    
    File /r /x *.svn .\programfiles\*

    ${StrRep} $0 $AppData "\" "/"
    !insertmacro _ReplaceInFile log4j.xml !{APPDATA} $0
    !insertmacro _ReplaceInFile log4jdebug.xml !{APPDATA} $0

    !insertmacro _ReplaceInFile howto.cfg !{VERSION} ${PROJECT_VERSION}
    !insertmacro _ReplaceInFile howto.cfg !{INSTDIR} $INSTDIR
    !insertmacro _ReplaceInFile howto.cfg !{APPDATA} $APPDATA
    !insertmacro _ReplaceInFile howtodebug.cfg !{VERSION} ${PROJECT_VERSION}
    !insertmacro _ReplaceInFile howtodebug.cfg !{INSTDIR} $INSTDIR
    !insertmacro _ReplaceInFile howtodebug.cfg !{APPDATA} $APPDATA

    File ..\..\target\mavenDependenciesForNSIS\*.jar
    File /x *source* ..\..\target\howto-${PROJECT_VERSION}.jar
     
    FileOpen $9 howto.bat w
    FileWrite $9 "@echo off$\r$\n"    
    FileWrite $9 "java "
    FileWrite $9 "-Dhowto.config=$\"$INSTDIR\howto.cfg$\" "
    FileWrite $9 "-jar $\"$INSTDIR\howto-${PROJECT_VERSION}.jar$\" "
    FileWrite $9 "%*$\r$\n"
     
    FileOpen $9 howtodebug.bat w
    FileWrite $9 "java "
    FileWrite $9 "-agentlib:jdwp=transport=dt_socket,server=y,suspend=y,address=8000 "
    FileWrite $9 "-Dhowto.config=$\"$INSTDIR\howtodebug.cfg$\" "
    FileWrite $9 "-jar $\"$INSTDIR\howto-${PROJECT_VERSION}.jar$\" "
    FileWrite $9 "%*$\r$\n"
        
    ${EnvVarUpdate} $0 "PATH" "A" "HKLM" $INSTDIR
    
    createDirectory $INSTDIR\plugins
    CreateDirectory "$SMPROGRAMS\Obdobion\${PROJECT_ARTIFACT_ID}"
    CreateDirectory "$AppData\Obdobion\${PROJECT_ARTIFACT_ID}"
    createShortCut "$SMPROGRAMS\Obdobion\${PROJECT_ARTIFACT_ID}\HowTo interactive console.lnk" \
    	  "cmd" \
     	  "/Q /F:OFF /D /C java \
     	    -Dhowto.config=$\"$INSTDIR\howto.cfg$\" \ 
     	    -jar $\"$INSTDIR\howto-${PROJECT_VERSION}.jar$\" System.interactiveConsole" \
     	  "" ""  SW_SHOWNORMAL \
     	  ALT|F1 \
    	  "An Interactive Console for HowTo"
    createShortCut "$SMPROGRAMS\Obdobion\${PROJECT_ARTIFACT_ID}\HowTo interactive console debug on 8000.lnk" \
    	  "cmd" \
     	  "/Q /F:OFF /D /C java \
     	    -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=8000 \
     	    -Dhowto.config=$\"$INSTDIR\howtodebug.cfg$\" \
     	    -jar $\"$INSTDIR\howto-${PROJECT_VERSION}.jar$\" System.interactiveConsole" \
     	  "" ""  SW_SHOWNORMAL \
     	  ALT|F2 \
    	  "An Interactive Console for HowTo with potential for debug on port 8000"
    createShortCut "$SMPROGRAMS\Obdobion\${PROJECT_ARTIFACT_ID}\HowTo Log.lnk" "$AppData\Obdobion\howto\howto.log" "" ""
    createShortCut "$SMPROGRAMS\Obdobion\${PROJECT_ARTIFACT_ID}\HowTo uninstall.lnk" "$INSTDIR\uninstall.exe" "" ""
    
    writeUninstaller "$INSTDIR\uninstall.exe"
SectionEnd

; Installer functions
Function .onInstSuccess

FunctionEnd

Section "uninstall"
    ${un.EnvVarUpdate} $0 "PATH" "R" "HKLM" $INSTDIR
    delete "$SMPROGRAMS\Obdobion\${PROJECT_ARTIFACT_ID}\*.lnk"
    delete $INSTDIR\*.bat
    delete $INSTDIR\*.jar
    delete $INSTDIR\log4j.xml
    delete $INSTDIR\*.exe
    rmdir /r $INSTDIR\plugins\*.*
SectionEnd

Function .onInit
    InitPluginsDir
FunctionEnd
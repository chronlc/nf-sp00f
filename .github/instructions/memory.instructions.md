---
applyTo: '**'
description: Workspace-specific AI memory for this project
lastOptimized: '2025-09-28T05:32:44.779586+00:00'
entryCount: 2
optimizationVersion: 1
autoOptimize: true
sizeThreshold: 50000
entryThreshold: 20
timeThreshold: 7
---
# Workspace AI Memory
This file contains workspace-specific information for AI conversations.

## Personal Context
- *(none specified)*

## Professional Context
- **2025-09-27 22:32:** Project: nf-sp00f33r Android EMV research app. Platform: Android 14+, Kotlin, Jetpack Compose (Material3), Min SDK 28.

## Technical Preferences
- Uses IsoDep for NFC reading and HostApduService for HCE emulation.
- Key modules: UI (Compose fragments), cardreading (NfcCardReaderWithWorkflows, EmvTlvParser), emulation (EmvAttackEmulationManager, EmulationProfiles), data models (EmvCardData, ApduLogEntry, CardProfile), utils (Hex/byte helpers), hardware (PN532 adapters).
- Use ByteArray for all APDU comms.
- PDOL/CDOL constructed dynamically from BER-TLV.
- EmulationProfiles: PPSE Poisoning, AIP Force Offline, Track2 Spoofing, Cryptogram Downgrade, CVM Bypass.

## Communication Preferences
- *(none specified)*

## Universal Laws
- No Kotlin safe-call operators in production.
- DELETE->REGENERATE protocol for corrupted files.
- Never append to files.
- Batch changes.
- Mark tasks complete only when build successful.
- Use terminal text editor approach for atomic write.

## Policies
- Build acceptance: Gradle assembleDebug must succeed.
- Store README.md and copilot-instructions.md as reference basis for future work.

## Suggestions/Hints
- Scripts folder planned with audit_codebase.py, pn532_terminal.py, etc.

## Memories/Facts
- **2025-09-27 22:32:** Project initialization and technical stack defined.- **2025-09-30 07:23:** Project status sync on 2025-09-30: BUILD SUCCESSFUL - Android scaffold complete, all systems operational, ready for next development phase

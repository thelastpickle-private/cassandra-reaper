--
--  Copyright 2018-2018 The Last Pickle Ltd
--
--  Licensed under the Apache License, Version 2.0 (the "License");
--  you may not use this file except in compliance with the License.
--  You may obtain a copy of the License at
--
--      http://www.apache.org/licenses/LICENSE-2.0
--
--  Unless required by applicable law or agreed to in writing, software
--  distributed under the License is distributed on an "AS IS" BASIS,
--  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
--  See the License for the specific language governing permissions and
--  limitations under the License.
--
-- Upgrade to allow active and inactive times
--

ALTER TABLE "repair_run"
ADD "active_time" TEXT;
ALTER TABLE "repair_run"
ADD "inactive_time" TEXT;
ALTER TABLE "repair_schedule"
ADD "active_time" TEXT;
ALTER TABLE "repair_schedule"
ADD "inactive_time" TEXT;
ALTER TABLE "repair_segment"
ADD "active_time" TEXT;
ALTER TABLE "repair_segment"
ADD "inactive_time" TEXT;
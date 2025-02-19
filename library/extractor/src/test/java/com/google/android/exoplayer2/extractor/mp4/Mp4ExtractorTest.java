/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.google.android.exoplayer2.extractor.mp4;

import com.google.android.exoplayer2.testutil.ExtractorAsserts;
import com.google.common.collect.ImmutableList;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.ParameterizedRobolectricTestRunner;
import org.robolectric.ParameterizedRobolectricTestRunner.Parameter;
import org.robolectric.ParameterizedRobolectricTestRunner.Parameters;

/** Tests for {@link Mp4Extractor}. */
@RunWith(ParameterizedRobolectricTestRunner.class)
public final class Mp4ExtractorTest {

  @Parameters(name = "{0}")
  public static ImmutableList<ExtractorAsserts.SimulationConfig> params() {
    return ExtractorAsserts.configs();
  }

  @Parameter public ExtractorAsserts.SimulationConfig simulationConfig;

  @Test
  public void mp4Sample() throws Exception {
    ExtractorAsserts.assertBehavior(Mp4Extractor::new, "media/mp4/sample.mp4", simulationConfig);
  }

  @Test
  public void mp4SampleWithSlowMotionMetadata() throws Exception {
    ExtractorAsserts.assertBehavior(
        Mp4Extractor::new, "media/mp4/sample_android_slow_motion.mp4", simulationConfig);
  }

  /**
   * Test case for https://github.com/google/ExoPlayer/issues/6774. The sample file contains an mdat
   * atom whose size indicates that it extends 8 bytes beyond the end of the file.
   */
  @Test
  public void mp4SampleWithMdatTooLong() throws Exception {
    ExtractorAsserts.assertBehavior(
        Mp4Extractor::new, "media/mp4/sample_mdat_too_long.mp4", simulationConfig);
  }

  @Test
  public void mp4SampleWithAc3Track() throws Exception {
    ExtractorAsserts.assertBehavior(
        Mp4Extractor::new, "media/mp4/sample_ac3.mp4", simulationConfig);
  }

  @Test
  public void mp4SampleWithAc4Track() throws Exception {
    ExtractorAsserts.assertBehavior(
        Mp4Extractor::new, "media/mp4/sample_ac4.mp4", simulationConfig);
  }

  @Test
  public void mp4SampleWithEac3Track() throws Exception {
    ExtractorAsserts.assertBehavior(
        Mp4Extractor::new, "media/mp4/sample_eac3.mp4", simulationConfig);
  }

  @Test
  public void mp4SampleWithEac3jocTrack() throws Exception {
    ExtractorAsserts.assertBehavior(
        Mp4Extractor::new, "media/mp4/sample_eac3joc.mp4", simulationConfig);
  }

  @Test
  public void mp4SampleWithOpusTrack() throws Exception {
    ExtractorAsserts.assertBehavior(
        Mp4Extractor::new, "media/mp4/sample_opus.mp4", simulationConfig);
  }

  @Test
  public void mp4SampleWithMha1Track() throws Exception {
    ExtractorAsserts.assertBehavior(
        Mp4Extractor::new, "media/mp4/sample_mpegh_mha1.mp4", simulationConfig);
  }

  @Test
  public void mp4SampleWithMhm1Track() throws Exception {
    ExtractorAsserts.assertBehavior(
        Mp4Extractor::new, "media/mp4/sample_mpegh_mhm1.mp4", simulationConfig);
  }

  @Test
  public void mp4SampleWithColorInfo() throws Exception {
    ExtractorAsserts.assertBehavior(
        Mp4Extractor::new, "media/mp4/sample_with_color_info.mp4", simulationConfig);
  }
}

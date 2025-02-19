# ExoPlayer VP9 extension #

The VP9 extension provides `LibvpxVideoRenderer`, which uses libvpx (the VPx
decoding library) to decode VP9 video.

## License note ##

Please note that whilst the code in this repository is licensed under
[Apache 2.0][], using this extension also requires building and including one or
more external libraries as described below. These are licensed separately.

[Apache 2.0]: https://github.com/google/ExoPlayer/blob/release-v2/LICENSE

## Build instructions (Linux, macOS) ##

To use this extension you need to clone the ExoPlayer repository and depend on
its modules locally. Instructions for doing this can be found in ExoPlayer's
[top level README][].

In addition, it's necessary to build the extension's native components as
follows:

* Set the following environment variables:

```
cd "<path to exoplayer checkout>"
EXOPLAYER_ROOT="$(pwd)"
VP9_EXT_PATH="${EXOPLAYER_ROOT}/extensions/vp9/src/main"
```

* Download the [Android NDK][] and set its location in an environment variable.
  This build configuration has been tested on NDK r21.

```
NDK_PATH="<path to Android NDK>"
```

* Fetch an appropriate branch of libvpx. We cannot guarantee compatibility
  with all versions of libvpx. We currently recommend version 1.8.0:

```
cd "<preferred location for libvpx>" && \
git clone https://chromium.googlesource.com/webm/libvpx && \
cd libvpx && \
git checkout tags/v1.8.0 -b v1.8.0 && \
LIBVPX_PATH="$(pwd)"
```

* Add a link to the libvpx source code in the vp9 extension `jni` directory and
  run a script that generates necessary configuration files for libvpx:

```
cd ${VP9_EXT_PATH}/jni && \
ln -s "$LIBVPX_PATH" libvpx && \
./generate_libvpx_android_configs.sh
```

* Build the JNI native libraries from the command line:

```
cd "${VP9_EXT_PATH}"/jni && \
${NDK_PATH}/ndk-build APP_ABI=all -j4
```

[top level README]: https://github.com/google/ExoPlayer/blob/release-v2/README.md
[Android NDK]: https://developer.android.com/tools/sdk/ndk/index.html

## Build instructions (Windows) ##

We do not provide support for building this extension on Windows, however it
should be possible to follow the Linux instructions in [Windows PowerShell][].

[Windows PowerShell]: https://docs.microsoft.com/en-us/powershell/scripting/getting-started/getting-started-with-windows-powershell

## Notes ##

* Every time there is a change to the libvpx checkout:
  * Android config scripts should be re-generated by running
    `generate_libvpx_android_configs.sh`
  * Clean and re-build the project.
* If you want to use your own version of libvpx, point to it with the
  `${VP9_EXT_PATH}/jni/libvpx` symlink. Please note that
  `generate_libvpx_android_configs.sh` and the makefiles may need to be modified
  to work with arbitrary versions of libvpx.

## Using the extension ##

Once you've followed the instructions above to check out, build and depend on
the extension, the next step is to tell ExoPlayer to use `LibvpxVideoRenderer`.
How you do this depends on which player API you're using:

* If you're passing a `DefaultRenderersFactory` to `ExoPlayer.Builder`, you can
  enable using the extension by setting the `extensionRendererMode` parameter of
  the `DefaultRenderersFactory` constructor to `EXTENSION_RENDERER_MODE_ON`.
  This will use `LibvpxVideoRenderer` for playback if `MediaCodecVideoRenderer`
  doesn't support decoding the input VP9 stream. Pass
  `EXTENSION_RENDERER_MODE_PREFER` to give `LibvpxVideoRenderer` priority over
  `MediaCodecVideoRenderer`.
* If you've subclassed `DefaultRenderersFactory`, add a `LibvpxVideoRenderer`
  to the output list in `buildVideoRenderers`. ExoPlayer will use the first
  `Renderer` in the list that supports the input media format.
* If you've implemented your own `RenderersFactory`, return a
  `LibvpxVideoRenderer` instance from `createRenderers`. ExoPlayer will use the
  first `Renderer` in the returned array that supports the input media format.
* If you're using `ExoPlayer.Builder`, pass a `LibvpxVideoRenderer` in the array
  of `Renderer`s. ExoPlayer will use the first `Renderer` in the list that
  supports the input media format.

Note: These instructions assume you're using `DefaultTrackSelector`. If you have
a custom track selector the choice of `Renderer` is up to your implementation,
so you need to make sure you are passing an `LibvpxVideoRenderer` to the
player, then implement your own logic to use the renderer for a given track.

## Using the extension in the demo application ##

To try out playback using the extension in the [demo application][], see
[enabling extension decoders][].

[demo application]: https://exoplayer.dev/demo-application.html
[enabling extension decoders]: https://exoplayer.dev/demo-application.html#enabling-extension-decoders

## Rendering options ##

There are two possibilities for rendering the output `LibvpxVideoRenderer`
gets from the libvpx decoder:

*   GL rendering using GL shader for color space conversion

    *   If you are using `SimpleExoPlayer` with `PlayerView`, enable this option
        by setting `surface_type` of `PlayerView` to be
        `video_decoder_gl_surface_view`.
    *   Otherwise, enable this option by sending `LibvpxVideoRenderer` a message
        of type `Renderer.MSG_SET_VIDEO_DECODER_OUTPUT_BUFFER_RENDERER` with an
        instance of `VideoDecoderOutputBufferRenderer` as its object.

*   Native rendering using `ANativeWindow`

    *   If you are using `SimpleExoPlayer` with `PlayerView`, this option is
        enabled by default.
    *   Otherwise, enable this option by sending `LibvpxVideoRenderer` a message
        of type `Renderer.MSG_SET_SURFACE` with an instance of `SurfaceView` as
        its object.

Note: Although the default option uses `ANativeWindow`, based on our testing the
GL rendering mode has better performance, so should be preferred.

## Links ##

*   [Javadoc][]: Classes matching `com.google.android.exoplayer2.ext.vp9.*`
    belong to this module.

[Javadoc]: https://exoplayer.dev/doc/reference/index.html

#extension GL_OES_standard_derivatives : enable
precision mediump float;
uniform highp vec4 BrushColor;
uniform highp vec4 PenThickness;
uniform highp vec4 PenColor;
uniform sampler2D PenTexture;
uniform sampler2D BrushTexture;
varying highp vec4 xlv_TEXCOORD0;
varying highp vec2 xlv_TEXCOORD1;
void main ()
{
  highp vec4 fillColor_1;
  highp vec4 textureColor_2;
  highp float tmpvar_3;
  highp vec2 tmpvar_4;
  tmpvar_4 = (vec2(0.5, 0.5) - xlv_TEXCOORD0.xy);
  tmpvar_3 = sqrt(dot (tmpvar_4, tmpvar_4));
  highp float tmpvar_5;
  tmpvar_5 = (xlv_TEXCOORD0.x - 0.5);
  highp float tmpvar_6;
  tmpvar_6 = (xlv_TEXCOORD0.y - 0.5);
  highp float fH_7;
  fH_7 = (tmpvar_3 + xlv_TEXCOORD1.x);
  fH_7 += 0.5;
  highp float tmpvar_8;
  tmpvar_8 = (PenThickness.x * 0.5);
  highp float tmpvar_9;
  tmpvar_9 = (abs(dFdx(fH_7)) + abs(dFdy(fH_7)));
  highp float tmpvar_10;
  tmpvar_10 = clamp ((abs(
    fract(fH_7)
  ) / (tmpvar_9 * tmpvar_8)), 0.0, 1.0);
  highp float tmpvar_11;
  tmpvar_11 = clamp (((1.0 - 
    abs(fract(fH_7))
  ) / (tmpvar_9 * tmpvar_8)), 0.0, 1.0);
  highp float tmpvar_12;
  tmpvar_12 = (1.0 - ((tmpvar_11 * 
    (tmpvar_11 * (3.0 - (2.0 * tmpvar_11)))
  ) * (tmpvar_10 * 
    (tmpvar_10 * (3.0 - (2.0 * tmpvar_10)))
  )));
  highp float tmpvar_13;
  highp float tmpvar_14;
  tmpvar_14 = (min (abs(
    (tmpvar_6 / tmpvar_5)
  ), 1.0) / max (abs(
    (tmpvar_6 / tmpvar_5)
  ), 1.0));
  highp float tmpvar_15;
  tmpvar_15 = (tmpvar_14 * tmpvar_14);
  tmpvar_15 = (((
    ((((
      ((((-0.01213232 * tmpvar_15) + 0.05368138) * tmpvar_15) - 0.1173503)
     * tmpvar_15) + 0.1938925) * tmpvar_15) - 0.3326756)
   * tmpvar_15) + 0.9999793) * tmpvar_14);
  tmpvar_15 = (tmpvar_15 + (float(
    (abs((tmpvar_6 / tmpvar_5)) > 1.0)
  ) * (
    (tmpvar_15 * -2.0)
   + 1.570796)));
  tmpvar_13 = (tmpvar_15 * sign((tmpvar_6 / tmpvar_5)));
  if ((abs(tmpvar_5) > (1e-8 * abs(tmpvar_6)))) {
    if ((tmpvar_5 < 0.0)) {
      if ((tmpvar_6 >= 0.0)) {
        tmpvar_13 += 3.141593;
      } else {
        tmpvar_13 = (tmpvar_13 - 3.141593);
      };
    };
  } else {
    tmpvar_13 = (sign(tmpvar_6) * 1.570796);
  };
  highp vec2 tmpvar_16;
  tmpvar_16.x = 0.5;
  tmpvar_16.y = (fract((tmpvar_13 / 6.28)) * xlv_TEXCOORD1.y);
  lowp vec4 tmpvar_17;
  tmpvar_17 = texture2D (PenTexture, tmpvar_16);
  textureColor_2 = tmpvar_17;
  lowp vec4 tmpvar_18;
  tmpvar_18 = texture2D (BrushTexture, xlv_TEXCOORD0.xy);
  fillColor_1 = (BrushColor * tmpvar_18);
  if (((tmpvar_3 + xlv_TEXCOORD1.x) > 0.5)) {
    fillColor_1 = vec4(0.0, 0.0, 0.0, 0.0);
  };
  highp vec4 tmpvar_19;
  tmpvar_19 = ((textureColor_2 * tmpvar_12) * PenColor);
  highp vec4 tmpvar_20;
  tmpvar_20 = mix (fillColor_1, tmpvar_19, tmpvar_19.wwww);
  gl_FragData[0] = tmpvar_20;
}


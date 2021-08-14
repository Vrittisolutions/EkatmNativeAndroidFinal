precision mediump float;
uniform highp vec4 PenThickness;
uniform highp vec4 NanPenColor;
uniform sampler2D PenTexture;
uniform sampler2D NanPenTexture;
varying highp vec4 xlv_COLOR0;
varying highp vec3 xlv_TEXCOORD0;
void main ()
{
  highp vec4 tmpvar_1;
  lowp vec4 tmpvar_2;
  tmpvar_2 = texture2D (PenTexture, xlv_TEXCOORD0.xy);
  highp vec4 tmpvar_3;
  tmpvar_3 = (xlv_COLOR0 * tmpvar_2);
  bool tmpvar_4;
  if ((xlv_TEXCOORD0.z < 0.0)) {
    highp float tmpvar_5;
    tmpvar_5 = abs(xlv_TEXCOORD0.y);
    tmpvar_4 = (tmpvar_5 < PenThickness.x);
  } else {
    tmpvar_4 = bool(0);
  };
  if (tmpvar_4) {
    tmpvar_1 = tmpvar_3;
  } else {
    lowp vec4 tmpvar_6;
    tmpvar_6 = texture2D (NanPenTexture, xlv_TEXCOORD0.xy);
    tmpvar_1 = mix (tmpvar_3, (NanPenColor * tmpvar_6), vec4(float((xlv_TEXCOORD0.y < 0.0))));
  };
  gl_FragData[0] = tmpvar_1;
}


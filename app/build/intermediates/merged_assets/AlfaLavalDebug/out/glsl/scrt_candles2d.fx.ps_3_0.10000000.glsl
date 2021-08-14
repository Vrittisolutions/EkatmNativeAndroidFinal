precision mediump float;
uniform highp vec4 NeutralColor;
uniform highp vec4 PenColorA;
uniform highp vec4 PenColorB;
uniform highp vec4 BrushColorA;
uniform highp vec4 BrushColorB;
uniform sampler2D BrushTextureA;
uniform sampler2D BrushTextureB;
varying highp vec4 xlv_COLOR0;
varying highp vec2 xlv_TEXCOORD0;
varying highp vec4 xlv_TEXCOORD1;
void main ()
{
  highp vec4 resultColor_1;
  if ((xlv_TEXCOORD1.z >= 0.0)) {
    resultColor_1 = mix (PenColorA, PenColorB, xlv_TEXCOORD1.wwww);
  } else {
    lowp vec4 tmpvar_2;
    tmpvar_2 = texture2D (BrushTextureA, xlv_TEXCOORD0);
    lowp vec4 tmpvar_3;
    tmpvar_3 = texture2D (BrushTextureB, xlv_TEXCOORD0);
    resultColor_1 = mix ((BrushColorA * tmpvar_2), (BrushColorB * tmpvar_3), xlv_TEXCOORD1.wwww);
  };
  highp vec4 tmpvar_4;
  tmpvar_4 = (xlv_COLOR0 - NeutralColor);
  highp float tmpvar_5;
  tmpvar_5 = dot (tmpvar_4, tmpvar_4);
  float tmpvar_6;
  if ((tmpvar_5 > 0.0)) {
    tmpvar_6 = 1.0;
  } else {
    tmpvar_6 = 0.0;
  };
  highp vec4 tmpvar_7;
  tmpvar_7 = mix (resultColor_1, xlv_COLOR0, vec4(tmpvar_6));
  gl_FragData[0] = tmpvar_7;
}


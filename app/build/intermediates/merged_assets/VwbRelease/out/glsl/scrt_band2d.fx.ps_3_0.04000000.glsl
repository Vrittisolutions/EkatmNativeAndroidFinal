precision mediump float;
uniform highp vec4 PenColorA;
uniform highp vec4 PenColorB;
uniform highp vec4 BrushColorA;
uniform highp vec4 BrushColorB;
uniform sampler2D PenTextureA;
uniform sampler2D PenTextureB;
uniform sampler2D BrushTextureA;
uniform sampler2D BrushTextureB;
varying highp vec4 xlv_COLOR0;
varying highp vec2 xlv_TEXCOORD0;
varying highp vec2 xlv_TEXCOORD1;
varying highp vec3 xlv_TEXCOORD2;
void main ()
{
  highp vec4 tmpvar_1;
  if ((xlv_TEXCOORD2.x != 0.0)) {
    highp vec4 ColorB_2;
    highp vec4 ColorA_3;
    lowp vec4 tmpvar_4;
    tmpvar_4 = texture2D (PenTextureA, xlv_TEXCOORD0);
    ColorA_3 = tmpvar_4;
    lowp vec4 tmpvar_5;
    tmpvar_5 = texture2D (PenTextureB, xlv_TEXCOORD0);
    ColorB_2 = tmpvar_5;
    highp vec4 tmpvar_6;
    tmpvar_6 = mix (ColorA_3, ColorB_2, xlv_TEXCOORD2.zzzz);
    highp vec4 tmpvar_7;
    if ((xlv_TEXCOORD2.y != 0.0)) {
      tmpvar_7 = xlv_COLOR0;
    } else {
      tmpvar_7 = mix (PenColorA, PenColorB, xlv_TEXCOORD2.zzzz);
    };
    tmpvar_1 = (tmpvar_6 * tmpvar_7);
  } else {
    lowp vec4 tmpvar_8;
    tmpvar_8 = texture2D (BrushTextureA, xlv_TEXCOORD1);
    highp vec4 tmpvar_9;
    tmpvar_9 = (BrushColorA * tmpvar_8);
    lowp vec4 tmpvar_10;
    tmpvar_10 = texture2D (BrushTextureB, xlv_TEXCOORD1);
    highp vec4 tmpvar_11;
    tmpvar_11 = (BrushColorB * tmpvar_10);
    highp vec4 tmpvar_12;
    if ((xlv_TEXCOORD2.y != 0.0)) {
      tmpvar_12 = xlv_COLOR0;
    } else {
      float tmpvar_13;
      if ((xlv_TEXCOORD0.x == 0.0)) {
        tmpvar_13 = 0.0;
      } else {
        tmpvar_13 = 1.0;
      };
      tmpvar_12 = mix (tmpvar_9, tmpvar_11, vec4(tmpvar_13));
    };
    tmpvar_1 = tmpvar_12;
  };
  gl_FragData[0] = tmpvar_1;
}


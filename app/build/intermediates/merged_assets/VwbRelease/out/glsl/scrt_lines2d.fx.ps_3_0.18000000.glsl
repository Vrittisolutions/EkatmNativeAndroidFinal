precision mediump float;
uniform highp vec4 BrushColor;
uniform sampler2D PenTexture;
uniform sampler2D BrushTexture;
varying highp vec4 xlv_COLOR0;
varying highp vec3 xlv_TEXCOORD0;
varying highp vec2 xlv_TEXCOORD1;
void main ()
{
  highp vec4 finalColor_1;
  lowp vec4 tmpvar_2;
  tmpvar_2 = texture2D (PenTexture, xlv_TEXCOORD0.xy);
  lowp vec4 tmpvar_3;
  tmpvar_3 = texture2D (BrushTexture, xlv_TEXCOORD1);
  finalColor_1 = ((xlv_COLOR0 * tmpvar_2) * (BrushColor * tmpvar_3));
  gl_FragData[0] = finalColor_1;
}


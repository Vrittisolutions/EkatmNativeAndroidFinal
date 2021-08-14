precision mediump float;
uniform highp vec4 BrushColor;
uniform sampler2D BrushTexture;
varying highp vec4 xlv_COLOR0;
varying highp vec2 xlv_TEXCOORD0;
void main ()
{
  lowp vec4 tmpvar_1;
  tmpvar_1 = texture2D (BrushTexture, xlv_TEXCOORD0);
  highp vec4 tmpvar_2;
  tmpvar_2 = ((xlv_COLOR0 * BrushColor) * tmpvar_1);
  gl_FragData[0] = tmpvar_2;
}


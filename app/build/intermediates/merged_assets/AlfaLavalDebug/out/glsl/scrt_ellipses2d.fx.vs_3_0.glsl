uniform highp mat4 matProj;
uniform highp mat4 matWorldView;
uniform highp vec4 PenThickness;
attribute highp vec2 vPosition;
attribute highp vec2 vTexCoord0;
attribute highp vec4 vColor;
attribute highp vec4 vTexCoord1;
varying highp vec4 xlv_TEXCOORD0;
varying highp vec4 xlv_COLOR0;
varying highp vec2 xlv_TEXCOORD1;
void main ()
{
  highp vec4 tmpvar_1;
  highp vec4 tmpvar_2;
  highp vec4 tmpvar_3;
  tmpvar_3.zw = vec2(0.0, 1.0);
  tmpvar_3.xy = vPosition;
  tmpvar_1 = (tmpvar_3 * (matWorldView * matProj));
  tmpvar_2.zw = vec2(0.0, 0.0);
  tmpvar_2.xy = vTexCoord0;
  tmpvar_1.y = -(tmpvar_1.y);
  highp vec2 tmpvar_4;
  tmpvar_4.x = PenThickness.x;
  tmpvar_4.y = (1.0/(PenThickness.y));
  gl_Position = tmpvar_1;
  xlv_TEXCOORD0 = tmpvar_2;
  xlv_COLOR0 = vColor;
  xlv_TEXCOORD1 = (vTexCoord1.xy * tmpvar_4);
}


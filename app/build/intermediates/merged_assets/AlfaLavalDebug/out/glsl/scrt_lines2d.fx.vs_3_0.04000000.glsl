uniform highp mat4 matProj;
uniform highp vec4 screenSize;
uniform highp mat4 matWorldView;
uniform highp mat4 matWorldViewProj;
uniform highp vec4 PenThickness;
uniform highp vec4 PenColor;
attribute highp vec3 vPosition;
attribute highp vec3 vTexCoord0;
attribute highp vec4 vColor1;
attribute highp vec3 vTexCoord1;
attribute highp vec4 vTexCoord4;
varying highp vec4 xlv_COLOR0;
varying highp vec3 xlv_TEXCOORD0;
varying highp vec2 xlv_TEXCOORD1;
void main ()
{
  highp vec4 tmpvar_1;
  highp vec4 tmpvar_2;
  tmpvar_2.zw = vec2(0.0, 1.0);
  tmpvar_2.xy = vPosition.xy;
  tmpvar_1 = (tmpvar_2 * (matWorldView * matProj));
  highp vec4 tmpvar_3;
  tmpvar_3 = vTexCoord4;
  highp vec4 tmpvar_4;
  tmpvar_4 = vec4(0.0, 0.0, 0.0, 0.0);
  highp vec4 outPosition_5;
  highp vec4 tmpvar_6;
  tmpvar_6.zw = vec2(0.0, 1.0);
  tmpvar_6.xy = vTexCoord0.xy;
  highp vec4 tmpvar_7;
  tmpvar_7 = (tmpvar_6 * matWorldViewProj);
  outPosition_5 = (tmpvar_7 / abs(tmpvar_7.w));
  highp vec4 tmpvar_8;
  tmpvar_8.zw = vec2(0.0, 1.0);
  tmpvar_8.xy = vTexCoord1.xy;
  highp vec4 tmpvar_9;
  tmpvar_9 = (tmpvar_8 * matWorldViewProj);
  highp vec2 tmpvar_10;
  tmpvar_10 = normalize(((tmpvar_9 / 
    abs(tmpvar_9.w)
  ).xy - outPosition_5.xy));
  outPosition_5.x = (outPosition_5.x - ((vTexCoord4.x * screenSize.z) * tmpvar_10.y));
  outPosition_5.y = (outPosition_5.y + ((vTexCoord4.x * screenSize.w) * tmpvar_10.x));
  if ((PenThickness.x <= 1.0)) {
    tmpvar_3.y = 0.5;
  };
  tmpvar_4 = (vColor1 * PenColor);
  highp vec3 tmpvar_11;
  tmpvar_11.x = tmpvar_3.y;
  tmpvar_11.y = tmpvar_3.w;
  tmpvar_11.z = tmpvar_3.z;
  tmpvar_1.xzw = outPosition_5.xzw;
  tmpvar_1.y = -(outPosition_5.y);
  gl_Position = tmpvar_1;
  xlv_COLOR0 = tmpvar_4;
  xlv_TEXCOORD0 = tmpvar_11;
  xlv_TEXCOORD1 = vec2(0.0, 0.0);
}


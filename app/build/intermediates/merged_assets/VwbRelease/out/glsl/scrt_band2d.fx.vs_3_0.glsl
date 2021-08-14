uniform highp mat4 matProj;
uniform highp vec4 screenSize;
uniform highp mat4 matWorldView;
uniform highp mat4 matWorldViewProj;
uniform highp mat4 matTexCoordTransform;
uniform highp vec4 PaletteSize;
uniform sampler2D PaletteTexture;
attribute highp vec3 vPosition;
attribute highp vec2 vTexCoord0;
attribute highp vec3 vTexCoord1;
attribute highp vec3 vTexCoord3;
attribute highp vec4 vTexCoord5;
attribute highp vec4 vTexCoord6;
varying highp vec4 xlv_COLOR0;
varying highp vec2 xlv_TEXCOORD0;
varying highp vec2 xlv_TEXCOORD1;
varying highp vec3 xlv_TEXCOORD2;
void main ()
{
  highp vec4 tmpvar_1;
  highp vec4 tmpvar_2;
  tmpvar_2.zw = vec2(0.0, 1.0);
  tmpvar_2.xy = vPosition.xy;
  tmpvar_1 = (tmpvar_2 * (matWorldView * matProj));
  highp vec3 tmpvar_3;
  tmpvar_3 = vec3(0.0, 0.0, 0.0);
  highp vec4 tmpvar_4;
  highp float tmpvar_5;
  tmpvar_5 = abs(vTexCoord5.z);
  if ((tmpvar_5 > 0.0)) {
    highp vec4 outPosition_6;
    highp vec4 tmpvar_7;
    tmpvar_7.zw = vec2(0.0, 1.0);
    tmpvar_7.xy = vTexCoord1.xy;
    highp vec4 tmpvar_8;
    tmpvar_8 = (tmpvar_7 * matWorldViewProj);
    outPosition_6 = (tmpvar_8 / abs(tmpvar_8.w));
    highp vec4 tmpvar_9;
    tmpvar_9.zw = vec2(0.0, 1.0);
    tmpvar_9.xy = vTexCoord3.xy;
    highp vec4 tmpvar_10;
    tmpvar_10 = (tmpvar_9 * matWorldViewProj);
    highp vec2 tmpvar_11;
    tmpvar_11 = normalize(((tmpvar_10 / 
      abs(tmpvar_10.w)
    ).xy - outPosition_6.xy));
    outPosition_6.x = (outPosition_6.x - ((vTexCoord5.x * screenSize.z) * tmpvar_11.y));
    outPosition_6.y = (outPosition_6.y + ((vTexCoord5.x * screenSize.w) * tmpvar_11.x));
    tmpvar_4 = outPosition_6;
  } else {
    highp vec4 finalPosition_12;
    highp vec4 tmpvar_13;
    tmpvar_13.zw = vec2(0.0, 1.0);
    tmpvar_13.xy = vTexCoord1.xy;
    highp vec4 tmpvar_14;
    tmpvar_14 = (tmpvar_13 * matWorldViewProj);
    highp vec4 tmpvar_15;
    tmpvar_15.zw = vec2(0.0, 1.0);
    tmpvar_15.xy = vTexCoord3.xy;
    highp vec4 tmpvar_16;
    tmpvar_16 = (tmpvar_15 * matWorldViewProj);
    finalPosition_12.xyw = mix ((tmpvar_14 / abs(tmpvar_14.w)), (tmpvar_16 / abs(tmpvar_16.w)), vTexCoord5.xxxx).xyw;
    finalPosition_12.z = 0.25;
    tmpvar_4 = finalPosition_12;
  };
  if ((vTexCoord5.z > 0.0)) {
    tmpvar_3.x = (2.0 / vTexCoord5.z);
  } else {
    tmpvar_3.x = 0.0;
  };
  tmpvar_3.y = (1.0/(tmpvar_3.x));
  tmpvar_3.z = vTexCoord6.x;
  highp vec2 tmpvar_17;
  tmpvar_17.x = vTexCoord5.y;
  tmpvar_17.y = vTexCoord5.w;
  highp vec2 _position_18;
  _position_18 = (vec2(0.5, 0.5) * (tmpvar_4.xy + vec2(1.0, 1.0)));
  highp vec2 tmpvar_19;
  highp vec4 tmpvar_20;
  tmpvar_20.zw = vec2(0.0, 1.0);
  tmpvar_20.xy = _position_18;
  tmpvar_19 = (matTexCoordTransform * tmpvar_20).xy;
  _position_18 = tmpvar_19;
  highp vec2 tmpvar_21;
  tmpvar_21.y = 0.5;
  tmpvar_21.x = (PaletteSize.z * (vTexCoord0.x + 0.5));
  highp vec4 tmpvar_22;
  lowp vec4 tmpvar_23;
  tmpvar_23 = texture2DLod (PaletteTexture, tmpvar_21, 0.0);
  tmpvar_22 = tmpvar_23;
  highp float tmpvar_24;
  tmpvar_24 = sqrt(dot (tmpvar_22, tmpvar_22));
  highp float tmpvar_25;
  if ((tmpvar_24 > 0.0)) {
    tmpvar_25 = 1.0;
  } else {
    tmpvar_25 = 0.0;
  };
  tmpvar_3.y = tmpvar_25;
  tmpvar_1.xzw = tmpvar_4.xzw;
  tmpvar_1.y = -(tmpvar_4.y);
  gl_Position = tmpvar_1;
  xlv_COLOR0 = tmpvar_22;
  xlv_TEXCOORD0 = tmpvar_17;
  xlv_TEXCOORD1 = tmpvar_19;
  xlv_TEXCOORD2 = tmpvar_3;
}

